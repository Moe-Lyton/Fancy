package com.example.fancy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fancy.data.Order
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.data.OrderDetails
import com.google.firebase.database.*
import com.example.fancy.Adapter.OrdersAdapter

class ViewOrdersActivity : AppCompatActivity() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var noOrdersTextView: TextView
    private lateinit var preparingButton: Button
    private lateinit var readyButton: Button

    private val ordersList = mutableListOf<Order>()
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("orders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        ordersRecyclerView = findViewById(R.id.recyclerViewOrders)
        noOrdersTextView = findViewById(R.id.textNoOrders)
        preparingButton = findViewById(R.id.btnMarkPreparing)
        readyButton = findViewById(R.id.btnMarkReady)

        ordersAdapter = OrdersAdapter(this, ordersList, object : OrdersAdapter.OnItemClickListener {
            override fun onItemClick(order: Order) {
                // Handle item click here
            }
        })

        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersRecyclerView.adapter = ordersAdapter

        preparingButton.setOnClickListener {
            markOrdersStatus("Preparing")
        }

        readyButton.setOnClickListener {
            markOrdersStatus("Ready")
        }

        loadOrders()
    }

    private fun loadOrders() {
        databaseReference.orderByChild("orderStatus").equalTo("Ongoing")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    ordersList.clear()

                    for (snapshot in dataSnapshot.children) {
                        val order = snapshot.getValue(Order::class.java)
                        order?.let {
                            it.orderId = snapshot.key
                            ordersList.add(it)
                        }
                    }

                    updateUI()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun updateUI() {
        if (ordersList.isEmpty()) {
            noOrdersTextView.text = "No ongoing orders"
            preparingButton.isEnabled = false
            readyButton.isEnabled = false
        } else {
            noOrdersTextView.text = ""
            preparingButton.isEnabled = true
            readyButton.isEnabled = true
        }

        ordersAdapter.notifyDataSetChanged()
    }

    private fun markOrdersStatus(status: String) {
        val selectedOrders = ordersAdapter.getSelectedOrders()

        for (order in selectedOrders) {
            order.orderStatus = status
            databaseReference.child(order.orderId.orEmpty()).setValue(order)
        }

        // Implement notification logic when status is "Ready"
        if (status == "Ready") {
            sendNotificationToCustomers(selectedOrders)
        }

        // Refresh UI after updating orders
        updateUI()
    }

    private fun sendNotificationToCustomers(orders: Set<Order>) {
        // Implement notification logic here (e.g., using Firebase Cloud Messaging)
        // Notify customers that their orders are ready for collection
    }
}


