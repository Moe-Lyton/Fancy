
package com.example.fancy.data

data class Payment(
    val paymentId: String? = null,
    val orderId: String? = null,
    val paymentType: String? = null,
    val amount: Double? = null,
    val paymentDate: Long? = null,
)
