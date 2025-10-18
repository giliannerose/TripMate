package com.example.tripmate

import android.content.Intent
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

        // Top Tabs navigation
        findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            val intent = Intent(this, TripDetailsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.tabPolls).setOnClickListener {
            val intent = Intent(this, CreatePollActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            val intent = Intent(this, ExpenseSummaryActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.tabDocs).setOnClickListener {
            val intent = Intent(this, DocumentsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            Toast.makeText(this, "You're already on Itinerary", Toast.LENGTH_SHORT).show()
        }

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)
        //----------

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
