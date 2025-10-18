package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val btnAccept = findViewById<Button>(R.id.btnAccept)
        val btnDecline = findViewById<Button>(R.id.btnDecline)
        val btnViewPoll = findViewById<Button>(R.id.btnViewPoll)
        val btnViewExpenses = findViewById<Button>(R.id.btnViewExpenses)
        val btnAcknowledge = findViewById<Button>(R.id.btnAcknowledge)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Confirmation dialog for Accept button
        btnAccept.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Accept Invitation")
                .setMessage("Are you sure you want to accept this invitation?")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Invitation accepted!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }

//  Confirmation dialog for Decline button
        btnDecline.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Decline Invitation")
                .setMessage("Are you sure you want to decline this invitation?")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Invitation declined.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }


        btnViewPoll.setOnClickListener {
            val intent = Intent(this, CreatePollActivity::class.java)
            startActivity(intent)
        }

        btnViewExpenses.setOnClickListener {
            val intent = Intent(this, ExpenseSummaryActivity::class.java)
            startActivity(intent)
        }

        btnAcknowledge.setOnClickListener {
            Toast.makeText(this, "Acknowledged.", Toast.LENGTH_SHORT).show()
        }

        //highlight notif
        bottomNav.selectedItemId = R.id.nav_notifications

        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_create -> {
                    val intent = Intent(this, MyTripsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_notifications -> {
                    Toast.makeText(this, "You're already on Notifications", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
}
