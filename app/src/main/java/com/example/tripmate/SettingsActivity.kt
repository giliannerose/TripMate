package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val tvBack = findViewById<TextView>(R.id.tvBack)
        val switchNotifications = findViewById<Switch>(R.id.switchNotifications)
        val tvPrivacyStatus = findViewById<TextView>(R.id.tvPrivacyStatus)
        val etCurrentPassword = findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSaveSettings = findViewById<Button>(R.id.btnSaveSettings)

        // Back to Profile
        tvBack.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Toggle notification state
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Notifications enabled" else "Notifications disabled"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Toggle privacy text
        tvPrivacyStatus.setOnClickListener {
            tvPrivacyStatus.text = if (tvPrivacyStatus.text == "Public") "Private" else "Public"
        }

        // Save Settings
        btnSaveSettings.setOnClickListener {
            val newPass = etNewPassword.text.toString()
            val confirmPass = etConfirmPassword.text.toString()

            if (newPass.isNotEmpty() && newPass == confirmPass) {
                Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show()
            } else if (newPass.isNotEmpty()) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Settings saved (no password change)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
