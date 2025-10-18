package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DocumentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

        val btnUpload = findViewById<Button>(R.id.btnUpload)
        val tabParticipants = findViewById<Button>(R.id.tabParticipants)
        val tabPolls = findViewById<Button>(R.id.tabPolls)
        val tabExpenses = findViewById<Button>(R.id.tabExpenses)
        val tabDocs = findViewById<Button>(R.id.tabDocs)
        val tabItinerary = findViewById<Button>(R.id.tabItinerary)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)
        //----------

        // Upload Button (temporary Toast)
        btnUpload.setOnClickListener {
            Toast.makeText(this, "Upload feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Tab navigation
        tabParticipants.setOnClickListener {
            startActivity(Intent(this, TripDetailsActivity::class.java))
        }
        tabPolls.setOnClickListener {
            startActivity(Intent(this, CreatePollActivity::class.java))
        }
        tabExpenses.setOnClickListener {
            startActivity(Intent(this, ExpenseSummaryActivity::class.java))
        }
        tabDocs.setOnClickListener {
            Toast.makeText(this, "You're already on Docs", Toast.LENGTH_SHORT).show()
        }
        tabItinerary.setOnClickListener {
            startActivity(Intent(this, ItineraryActivity::class.java))
        }

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, DashboardActivity::class.java))
                R.id.nav_create -> startActivity(Intent(this, MyTripsActivity::class.java))
                R.id.nav_notifications -> startActivity(Intent(this, NotificationsActivity::class.java))
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            overridePendingTransition(0, 0)
            true
        }
    }
}
