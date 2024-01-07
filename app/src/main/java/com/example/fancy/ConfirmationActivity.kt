package com.example.fancy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fancy.data.Feedback
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.example.fancy.databinding.ActivityConfirmationBinding

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = Firebase.database.reference

        val orderId = intent.getStringExtra("ORDER_ID") ?: "Unavailable"
        val customerId = intent.getStringExtra("CUSTOMER_ID") ?: "Unavailable"
        val orderTime = intent.getStringExtra("ORDER_TIME") ?: "Unavailable"
        val orderStatus = intent.getStringExtra("ORDER_STATUS") ?: "Unavailable"

        binding.orderId.text = orderId
        binding.customerId.text = customerId
        binding.orderTime.text = orderTime
        binding.orderStatus.text = orderStatus

        databaseReference.child("Customers").child(auth.currentUser?.uid ?: "").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val username = dataSnapshot.child("cusUserName").value
                binding.confirmationMessage.text = "$username your order has been placed successfully"
            }
        }

        binding.submitFeedbackButton.setOnClickListener {
            submitFeedback(orderId)
        }

        binding.menuButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        binding.viewProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


    }

    private fun submitFeedback(orderId: String) {
        val userId = auth.currentUser?.uid ?: return
        val rating = binding.ratingBar.rating
        val comments = binding.feedbackInput.text.toString()

        val feedback = Feedback(userId, orderId, rating, comments)

        val databaseReference = FirebaseDatabase.getInstance().getReference("Feedback")
        val feedbackId = databaseReference.push().key ?: return
        databaseReference.child(feedbackId).setValue(feedback).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
            }
        }
    }
}