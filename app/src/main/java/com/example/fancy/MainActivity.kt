package com.example.fancy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fancy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    // Declare the variables

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContentView(binding.root)


        binding.loginButton.setOnClickListener {
            val email = binding.loginTextEmailAddress.text.toString()
            val password = binding.loginTextPassword.text.toString()
            loginUser(email, password)
        }

        binding.registerLink.setOnClickListener {
            val registerIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registerIntent)
        }

    }

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Authentication Passed!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Redirect to the menu page
                        val menuIntent = Intent(this, MenuActivity::class.java)
                        startActivity(menuIntent)
                    } else {
                        // Login failed, display an error message to the user.
                        Toast.makeText(
                            this, "Authentication failed. Check your email and password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // If the email or password is empty, display a message to the user.
            Toast.makeText(
                this, "Please enter text in email and password fields.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}