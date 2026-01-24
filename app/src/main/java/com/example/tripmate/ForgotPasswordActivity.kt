package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSendResetLink = findViewById<Button>(R.id.btnSendResetLink)
        val tvBackLogin = findViewById<TextView>(R.id.tvBackLogin)

        btnSendResetLink.setOnClickListener {
            val email = etEmail.text.toString().trim()


            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Email format validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            Toast.makeText(this, "Password reset link sent to $email", Toast.LENGTH_LONG).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        tvBackLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
