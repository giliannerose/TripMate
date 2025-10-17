package com.example.tripmate

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ItineraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerary)

        val btnAddActivity = findViewById<Button>(R.id.btnAddActivity)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        btnAddActivity.setOnClickListener {
            Toast.makeText(this, "Add new activity clicked!", Toast.LENGTH_SHORT).show()
        }

        // Tab navigation (placeholders)
        val tabs = listOf(
            R.id.tabParticipants,
            R.id.tabPolls,
            R.id.tabExpenses,
            R.id.tabDocs,
            R.id.tabItinerary
        )

        for (tab in tabs) {
            findViewById<Button>(tab).setOnClickListener {
                val tabName = resources.getResourceEntryName(tab)
                Toast.makeText(this, "$tabName tab clicked", Toast.LENGTH_SHORT).show()
            }
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
