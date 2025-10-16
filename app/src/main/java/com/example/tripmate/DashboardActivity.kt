package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // References to UI elements
        val cardTripSiargao = findViewById<CardView>(R.id.cardTripSiargao)
        val btnViewItinerary = findViewById<Button>(R.id.btnViewItinerary)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // When user clicks anywhere on the Siargao trip card
        cardTripSiargao.setOnClickListener {
            val intent = Intent(this, TripDetailsActivity::class.java)
            startActivity(intent)
        }

        // When user clicks the View Itinerary button (optional)
        btnViewItinerary.setOnClickListener {
            Toast.makeText(this, "You tapped the button inside the card!", Toast.LENGTH_SHORT).show()
        }

        // Bottom navigation interactions
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
