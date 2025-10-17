package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val btnAccept = findViewById<Button>(R.id.btnAccept)
        val btnTentative = findViewById<Button>(R.id.btnTentative)
        val btnDecline = findViewById<Button>(R.id.btnDecline)
        val btnViewPoll = findViewById<Button>(R.id.btnViewPoll)
        val btnViewExpenses = findViewById<Button>(R.id.btnViewExpenses)
        val btnAcknowledge = findViewById<Button>(R.id.btnAcknowledge)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        btnAccept.setOnClickListener {
            Toast.makeText(this, "Invitation accepted!", Toast.LENGTH_SHORT).show()
        }

        btnTentative.setOnClickListener {
            Toast.makeText(this, "Marked as tentative.", Toast.LENGTH_SHORT).show()
        }

        btnDecline.setOnClickListener {
            Toast.makeText(this, "Invitation declined.", Toast.LENGTH_SHORT).show()
        }

        btnViewPoll.setOnClickListener {
            Toast.makeText(this, "Opening Poll...", Toast.LENGTH_SHORT).show()
        }

        btnViewExpenses.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
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
                R.id.nav_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}
