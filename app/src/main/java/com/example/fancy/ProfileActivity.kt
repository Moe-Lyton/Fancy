package com.example.fancy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fancy.Adapter.ProfileAdapter
import com.example.fancy.data.Customer
import com.example.fancy.data.Order
import com.example.fancy.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var auth: FirebaseAuth
    private var databaseReference = FirebaseDatabase.getInstance().getReference("Order")
    private var databaseReference2 = FirebaseDatabase.getInstance().getReference("Customers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        getCustomerDetails()
        setupRecyclerView()
        getOrderHistory()

        binding.btnViewMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun setupRecyclerView() {
        profileAdapter = ProfileAdapter()
        binding.recyclerViewOrderHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrderHistory.adapter = profileAdapter
    }

    private fun getCustomerDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            databaseReference2.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val customer = snapshot.getValue(Customer::class.java)
                    updateCustomerDetails(customer)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateCustomerDetails(customer: Customer?) {
        customer?.let {
            binding.profileFullName.text = "Full Name: ${it.cusFullName}"
            binding.profileUserName.text = "Name: ${it.cusUserName}"
            binding.profileEmail.text = "Email: ${it.cusEmail}"
            binding.profilePhone.text = "Phone: ${it.cusPhoneNo}"
        }
    }

    private fun getOrderHistory() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            databaseReference.orderByChild("cusId").equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val orders = mutableListOf<Order>()
                        for (orderSnapshot in snapshot.children) {
                            val order = orderSnapshot.getValue(Order::class.java)
                            order?.let {
                                orders.add(it)
                            }
                        }
                        profileAdapter.setOrders(orders)
                        checkEmptyState(orders)
                    }

                    override fun onCancelled(databaseErroe: DatabaseError) {
                        Toast.makeText(this@ProfileActivity, "Error: ${databaseErroe.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun checkEmptyState(orders: List<Order>) {
        if (orders.isEmpty()) {
            binding.recyclerViewOrderHistory.isVisible = false
            binding.profileEmptyOrderHistory.isVisible = true
        } else {
            binding.recyclerViewOrderHistory.isVisible = true
            binding.profileEmptyOrderHistory.isVisible = false
        }
    }
}

