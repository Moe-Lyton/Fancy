 package com.example.fancy.data

 data class CartItem(
     val cartItemId: String? = null,
     val productId: String? = null,
     val productName: String? = null,
     var productPrice: Double? = null,
     val prodImage: String? = null,
 )