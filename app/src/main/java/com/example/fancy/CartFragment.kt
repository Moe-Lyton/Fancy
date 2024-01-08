import com.example.fancy.data.OrderDetails

/*
package com.example.fancy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.Adapter.CartItemAdapter
import com.example.fancy.data.Order
import com.example.fancy.data.OrderDetails
import com.example.fancy.data.Product
import com.example.fancy.repository.orderRepository
import com.google.firebase.database.FirebaseDatabase

class CartFragment : Fragment() {

    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var btnAddMoreItems: Button
    private lateinit var btnChoosePayment: Button
    private lateinit var btnPlaceOrder: Button
    private lateinit var textToTotalPrice: TextView

    // Reference to the database
    private val databaseReference = FirebaseDatabase.getInstance().getReference("cartItems")


    private fun navigateToMenu() {
        val intent = Intent(requireActivity(), MenuActivity::class.java)
        startActivity(intent)
    }

    /*
    private fun navigateToPayment() {
        val intent = Intent(requireActivity(), PaymentActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToOrder() {
        val intent = Intent(requireActivity(), OrderActivity::class.java)
        startActivity(intent)
    }

     */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        //inflate the layout
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        //initialize views
        val recyclerViewCart: RecyclerView = view.findViewById(R.id.recyclerViewCart)
        btnAddMoreItems = view.findViewById(R.id.btnAddMoreItems)
        btnChoosePayment = view.findViewById(R.id.btnChoosePayment)
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder)
        textToTotalPrice = view.findViewById(R.id.textTotalPrice)

        //set up recyclerview
        cartItemAdapter = CartItemAdapter(getCartItems()) { product ->
            //remove product from cart
            removeFromCart(product)
            }

        recyclerViewCart.adapter = cartItemAdapter
        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())

        /*
        btnChoosePayment.setOnClickListener {
            //navigate to payment activity
            navigateToPayment()
        }

        btnPlaceOrder.setOnClickListener {
            //navigate to order activity
            navigateToOrder()
            placeOrder()
        }

         */


        updateTotalPrice()
        return view

    }


    private fun getCartItems(): List<Product> {
        //return list of products from firebase
        return Product.getProducts()
    }

    private fun removeFromCart(product: Product) {
        //remove product from cart
        databaseReference.child(product.prodId).removeValue()
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        //update total price
        val totalPrice = getCartItems().sumByDouble { it.prodPrice }
        textToTotalPrice.text = "£$totalPrice"
    }

    //Implement logic to place the order in Firebase Realtime Database
    private fun placeOrder() {
        //get cart items
        val cartItems = getCartItems()
        //get total price
        val totalPrice = cartItems.sumByDouble { it.prodPrice }
        //create order
        val order = Order(cartItems, totalPrice)
        //place order in firebase
        orderRepository.placeOrder(order)
*/

/*
        //Implement logic to place the order in Firebase Realtime Database
        val customerId = "1"

        // Assuming 'orderId' is generated uniquely for each order
        val orderId = databaseReference.push().key

        // Create an order object
        FirebaseDatabase.getInstance().getReference("orders")
            .child(customerId)
            .child(orderId!!)
            .setValue(order)

        // Optionally, you can also save the order details in a separate node
        val orderDetails = OrderDetails(orderId, "1")
        FirebaseDatabase.getInstance().getReference("orderDetails").child(orderId).setValue(orderDetails)
    }





}
*/