package com.example.fancy

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fancy.data.Customer
import com.example.fancy.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            registerUser()
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val fullName = binding.registerTextFullName.text.toString().trim()
        val email = binding.registerTextEmailAddress.text.toString().trim()
        val phone = binding.registerTextPhone.text.toString().trim()
        val userName = binding.registerTextUserName.text.toString().trim()
        val password = binding.registerTextPassword.text.toString()
        val password2 = binding.registerTextPassword2.text.toString()

        if (validateInput(fullName, email, phone, userName, password, password2)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val cusId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                        val customer = Customer(cusId, fullName, email, password, password2, phone, userName, true)
                        uploadCustomerData(customer)
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateInput(fullName: String, email: String, phone: String, userName: String, password: String, password2: String): Boolean {
        when {
            fullName.isEmpty() -> {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
                return false
            }
            email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return false
            }
            phone.isEmpty() || phone.length != 11 -> {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                return false
            }
            userName.isEmpty() -> {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
                return false
            }
            password.isEmpty() || password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return false
            }
            password != password2 -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

    private fun uploadCustomerData(customer: Customer) {
        FirebaseDatabase.getInstance().getReference("Customers")
            .child(customer.cusId!!)
            .setValue(customer)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload customer data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}