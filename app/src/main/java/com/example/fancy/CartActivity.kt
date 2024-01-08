package com.example.fancy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fancy.Adapter.CartAdapter
import com.example.fancy.data.CartItem
import com.example.fancy.data.Order
import com.example.fancy.data.Payment
import com.example.fancy.databinding.ActivityCartBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.reference

        binding.recyclerViewCart.adapter = CartAdapter(cartItems) { cartItem ->
            removeFromCart(cartItem)
        }
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)

        getCartItems()

        binding.btnAddMoreItems.setOnClickListener {
            // Navigate back to MenuActivity or product selection screen
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        setupPaymentMethod()
        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }

    }


    private fun removeFromCart(cartItem: CartItem) {
        val userId = auth.currentUser?.uid ?: return
        cartItem.cartItemId?.let {
            databaseReference.child("cart").child(userId).child(
                it
            )
        }?.removeValue()
    }


    private fun setupPaymentMethod() {
        binding.radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.radioCash -> {
                    Toast.makeText(this, "Cash on collection", Toast.LENGTH_SHORT).show()
                }
                R.id.radioCard -> {
                    Toast.makeText(this, "Card on collection", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //assume
    private fun placeOrder() {
        // Check if the cart is empty
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Please add items to your cart.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if a payment method is selected
        val selectedPaymentType = when (binding.radioGroupPayment.checkedRadioButtonId) {
            R.id.radioCash -> "Cash"
            R.id.radioCard -> "Card"
            else -> {
                Toast.makeText(this, "Please select a payment method.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Rest of the order placement logic
        val order = Order(
            orderId = null,
            cusId = auth.currentUser?.uid,
            orderTime = System.currentTimeMillis(),
            orderStatus = "Pending"
        )

        val orderRef = databaseReference.child("Order")
        val orderKey = orderRef.push().key ?: return
        orderRef.child(orderKey).setValue(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val payment = Payment(
                    paymentId = null,
                    orderId = orderKey,
                    paymentType = selectedPaymentType,
                    amount = calculateTotalPrice(),
                    paymentDate = System.currentTimeMillis()
                )
                val paymentRef = databaseReference.child("Payment")
                paymentRef.push().setValue(payment)
                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()

                // Navigate to the confirmation screen
                val intent = Intent(this, ConfirmationActivity::class.java)
                intent.putExtra("ORDER_ID", orderKey)
                intent.putExtra("CUSTOMER_ID", auth.currentUser?.uid)
                intent.putExtra("ORDER_TIME", order.orderTime)
                intent.putExtra("ORDER_STATUS", order.orderStatus)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
            }

            // Clear the cart
            databaseReference.child("cart").child(auth.currentUser?.uid ?: return@addOnCompleteListener).removeValue()
        }
    }



    private fun getCartItems() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = databaseReference.child("cart").child(userId)

        cartRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()
                snapshot.children.forEach { child ->
                    val cartItem = child.getValue(CartItem::class.java)
                    cartItem?.let { cartItems.add(it) }
                }
                binding.recyclerViewCart.adapter?.notifyDataSetChanged()
                updateTotalPrice()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Error fetching cart items.", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun calculateTotalPrice(): Double {
        return cartItems.sumOf { it.productPrice ?: 0.0 }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice() {
        binding.textTotalPrice.text = "£${calculateTotalPrice()}"
    }

}