package com.example.fancy.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.data.Order
import com.example.fancy.databinding.ProfileOrderDetailsBinding


class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.OrderViewHolder>() {

    private var orders = listOf<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ProfileOrderDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size


    fun setOrders(orders: MutableList<Order>) {
        this.orders = orders
        notifyDataSetChanged()

    }

    class OrderViewHolder(private val binding: ProfileOrderDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.textViewOrderId.text = "Order ID: ${order.orderId}"
            binding.textViewCustomerId.text = "Customer ID: ${order.cusId}"
            binding.textViewOrderTime.text = "Order Time: ${order.orderTime}"
            binding.textViewOrderStatus.text = "Order Status: ${order.orderStatus}"
        }
    }
}


