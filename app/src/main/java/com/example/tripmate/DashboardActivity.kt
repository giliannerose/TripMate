package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
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

        // Quick Actions
        val actionCreate = findViewById<LinearLayout>(R.id.actionCreate)
        val actionInvite = findViewById<LinearLayout>(R.id.actionInvite)
        val actionExpenses = findViewById<LinearLayout>(R.id.actionExpenses)

        // When user clicks anywhere on the Siargao trip card
        cardTripSiargao.setOnClickListener {
            val intent = Intent(this, TripDetailsActivity::class.java)
            startActivity(intent)
        }

        // When user clicks the View Itinerary button
        btnViewItinerary.setOnClickListener {
            val intent = Intent(this, ItineraryActivity::class.java)
            startActivity(intent)
        }

        // Quick Actions (temporary Toasts for now)
        actionCreate.setOnClickListener {
            val intent = Intent(this, CreateTripActivity::class.java)
            startActivity(intent)
        }

        actionInvite.setOnClickListener {
            val intent = Intent(this, InviteMembersActivity::class.java)
            startActivity(intent)
        }

        actionExpenses.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        // Bottom navigation interactions
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()

                R.id.nav_create -> {
                    val intent = Intent(this, MyTripsActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_notifications -> Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}
