/*

package com.example.fancy.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.fancy.data.Product

class productRepository {
    private val database: DatabaseReference = Firebase.database.reference.child("products")

    fun addProduct(product: Product) {
        database.child(product.prodId).setValue(product)
    }

    fun updateProduct(product: Product) {
        database.child(product.prodId).setValue(product)
    }

    fun deleteProduct(product: Product) {
        database.child(product.prodId).removeValue()
    }

    fun getProduct(prodId: String) {
        database.child(prodId).get()
    }

    fun getAllProducts() {
        database.get()
    }



}

 */





