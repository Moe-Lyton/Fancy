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

    private val selectedOrders = mutableSetOf<Order>()

    fun getSelectedOrders(): Set<Order> {
        return selectedOrders
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

        init {
            // Set click listener for the entire item view
            itemView.setOnClickListener {
                val order = ordersList[adapterPosition]
                // Toggle the selection state
                if (selectedOrders.contains(order)) {
                    selectedOrders.remove(order)
                } else {
                    selectedOrders.add(order)
                }
                // Notify the item click listener
                itemClickListener.onItemClick(order)
                // Update the UI to reflect the selection
                notifyItemChanged(adapterPosition)
            }

            // Set click listener for the checkbox
            checkBoxOrder.setOnCheckedChangeListener { _, isChecked ->
                val order = ordersList[adapterPosition]
                // Toggle the selection state
                if (isChecked) {
                    selectedOrders.add(order)
                } else {
                    selectedOrders.remove(order)
                }
                // Notify the item click listener
                itemClickListener.onItemClick(order)
            }
        }

        fun bind(order: Order) {
            // Bind data to views
            textOrderId.text = context.getString(R.string.order_id, order.orderId)
            textOrderStatus.text = context.getString(R.string.order_status, order.orderStatus)

            // Set the checkbox state based on the selection
            checkBoxOrder.isChecked = selectedOrders.contains(order)
        }
    }
}


