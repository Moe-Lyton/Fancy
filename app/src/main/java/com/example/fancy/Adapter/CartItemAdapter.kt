package com.example.fancy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.R
import com.example.fancy.data.Product

class CartItemAdapter(

    private val cartItemList: List<Product>,
    private val onRemoveItemClick: (Product) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //define the views here
        val productName: TextView = itemView.findViewById(R.id.textProductName)
        val productPrice: TextView = itemView.findViewById(R.id.textProductPrice)
        val productImage: ImageView = itemView.findViewById(R.id.imageProduct)
        val addToCartButton: Button = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : ViewHolder {
        // inflate the layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)  //false because we don't want to attach it to the root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = cartItemList[position]

        holder.productName.text = product.prodName
        holder.productPrice.text =  "£${product.prodPrice}"
        //holder.productImage.setImageResource(product.prodImage)
        /*
        Glide.with(holder.productImage.context)
            .load(product.prodImage)
            .into(holder.productImage)

         */

        holder.addToCartButton.setOnClickListener {
            onRemoveItemClick(product)
        }
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }
}