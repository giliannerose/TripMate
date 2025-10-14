package com.example.tripmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnGetStarted = findViewById<Button>(R.id.btnGetStarted)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnGetStarted.setOnClickListener {
            // Navigate to Sign Up
        }

        btnLogin.setOnClickListener {
            // Navigate to Login
        }
    }
}
