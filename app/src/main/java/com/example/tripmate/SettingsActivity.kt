package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

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

        btnSaveSettings.setOnClickListener {

            val dialog = AlertDialog.Builder(this)
                .setTitle("Save Settings")
                .setMessage("Are you sure you want to save these changes?")
                .setPositiveButton("Yes", null) // override later
                .setNegativeButton("Cancel") { d, _ ->
                    d.dismiss()
                    Toast.makeText(this, "Save canceled.", Toast.LENGTH_SHORT).show()
                }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                    val currentPass = etCurrentPassword.text.toString().trim()
                    val newPass = etNewPassword.text.toString().trim()
                    val confirmPass = etConfirmPassword.text.toString().trim()

                    var isValid = true


                    if (newPass.isNotEmpty() || confirmPass.isNotEmpty()) {

                        if (currentPass.isEmpty()) {
                            etCurrentPassword.error = "Current password is required"
                            isValid = false
                        }

                        if (newPass.length < 6) {
                            etNewPassword.error = "Password must be at least 6 characters"
                            isValid = false
                        }

                        if (newPass != confirmPass) {
                            etConfirmPassword.error = "Passwords do not match"
                            isValid = false
                        }
                    }

                    if (isValid) {
                        Toast.makeText(
                            this,
                            "Settings saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()

                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            dialog.show()
        }

    }
}

