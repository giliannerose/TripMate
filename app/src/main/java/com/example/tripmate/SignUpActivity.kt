package com.example.tripmate

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val btnBackLogin = findViewById<Button>(R.id.btnBackLogin)

        btnCreateAccount.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            var isValid = true

            // Name validation
            if (name.isEmpty()) {
                etName.error = "Name is required"
                isValid = false
            }

            // Email validation
            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email address"
                isValid = false
            }

            // Password validation
            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                isValid = false
            } else if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Account created for $name!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ProfileSetupActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        btnBackLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
