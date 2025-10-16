package com.example.tripmate

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnViewItinerary = findViewById<Button>(R.id.btnViewItinerary)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        btnViewItinerary.setOnClickListener {
            Toast.makeText(this, "Opening Siargao Itinerary...", Toast.LENGTH_SHORT).show()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                R.id.nav_create -> Toast.makeText(this, "Create Trip", Toast.LENGTH_SHORT).show()
                R.id.nav_notifications -> Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}
