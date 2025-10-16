package com.example.tripmate

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class TripDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        val tabParticipants = findViewById<Button>(R.id.tabParticipants)
        val tabPolls = findViewById<Button>(R.id.tabPolls)
        val tabExpenses = findViewById<Button>(R.id.tabExpenses)
        val tabDocs = findViewById<Button>(R.id.tabDocs)
        val tabItinerary = findViewById<Button>(R.id.tabItinerary)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Tab clicks
        tabParticipants.setOnClickListener { Toast.makeText(this, "Participants tab selected", Toast.LENGTH_SHORT).show() }
        tabPolls.setOnClickListener { Toast.makeText(this, "Polls tab selected", Toast.LENGTH_SHORT).show() }
        tabExpenses.setOnClickListener { Toast.makeText(this, "Expenses tab selected", Toast.LENGTH_SHORT).show() }
        tabDocs.setOnClickListener { Toast.makeText(this, "Docs tab selected", Toast.LENGTH_SHORT).show() }
        tabItinerary.setOnClickListener { Toast.makeText(this, "Itinerary tab selected", Toast.LENGTH_SHORT).show() }

        // Bottom nav interactions
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
