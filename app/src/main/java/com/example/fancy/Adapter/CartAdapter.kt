
package com.example.fancy.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.R
import com.example.fancy.data.CartItem
import com.example.fancy.databinding.ItemCartProductBinding

class CartAdapter(private val cartItems: List<CartItem>, private val onItemRemoved: (CartItem) -> Unit) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCartProductBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(cartItem: CartItem) {
            binding.textProductName.text = cartItem.productName
            binding.textProductPrice.text = "£${cartItem.productPrice}"
            //binding.imageProduct.setImageResource(cartItem.prodImage)
            binding.btnRemoveFromCart.setOnClickListener {
                onItemRemoved(cartItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

}

