package com.example.fancy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fancy.Adapter.ProductAdapter
import com.example.fancy.data.CartItem
import com.example.fancy.data.Product
import com.example.fancy.databinding.ActivityMenuBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        databaseReference = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        // Set welcome text with current user username from firebase realtime database
        databaseReference.child("Customers").child(auth.currentUser?.uid ?: "").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val username = dataSnapshot.child("cusUserName").value
                binding.textViewWelcome.text = "Welcome $username"
            }
        }




        // Setup RecyclerView
        getProducts { productList ->
            productAdapter = ProductAdapter(productList) { product ->
                addToCart(product)
                Toast.makeText(this, "Added ${product.prodName} to cart", Toast.LENGTH_SHORT).show()

            }

            binding.recyclerViewMenu.adapter = productAdapter
            binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this)
        }

        // handle navigation to cart - this is the cart button
        binding.buttonCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

// handle navigation to profile - this is the profile button
        binding.imageViewProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }

    private fun addToCart(product: Product) {
        // Add product to cart
        val cartItemId = databaseReference.push().key
        val cartItem = CartItem(cartItemId!!, product.prodId, product.prodName, product.prodPrice, product.prodImage)
        databaseReference.child("cart").child(auth.currentUser?.uid ?: "").child(cartItemId).setValue(cartItem)
        //databaseReference.child("cart").child(cartItemId).setValue(cartItem)
    }

    private fun getProducts(callback: (List<Product>) -> Unit) {
        val productList = mutableListOf<Product>()

        databaseReference.child("products").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let { productList.add(it) }
                }

                // Invoke the callback with the productList
                callback(productList)
            }
        }
    }

}





