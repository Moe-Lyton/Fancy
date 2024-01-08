package com.example.fancy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fancy.R
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.data.Order
import androidx.core.content.ContextCompat
import android.graphics.Color
import android.widget.CheckBox


class OrdersAdapter(
    private val context: Context,
    private val ordersList: List<Order>,
    private var itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(order: Order)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun getSelectedOrders(): Set<Order> {
        // Implement logic to get selected orders
        return emptySet()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views here
        private val checkBoxOrder: CheckBox = itemView.findViewById(R.id.checkBoxOrder)
        private val textOrderId: TextView = itemView.findViewById(R.id.textOrderId)
        private val textOrderStatus: TextView = itemView.findViewById(R.id.textOrderStatus)

        fun bind(order: Order) {
            // Bind data to views
            textOrderId.text = "Order ID: ${order.orderId}"
            textOrderStatus.text = "Status: ${order.orderStatus}"

            // Set click listener
            itemView.setOnClickListener { itemClickListener.onItemClick(order) }

            // You may want to handle CheckBox clicks if needed
            checkBoxOrder.setOnCheckedChangeListener { _, isChecked ->
                // Handle checkbox state change if needed
            }
        }
    }
}