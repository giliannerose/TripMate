package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyTripsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trips)

        val btnCreateTrip = findViewById<Button>(R.id.btnCreateTrip)
        val cardSiargao = findViewById<CardView>(R.id.cardSiargao)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        btnCreateTrip.setOnClickListener {
            val intent = Intent(this, CreateTripActivity::class.java)
            startActivity(intent)
        }

        cardSiargao.setOnClickListener {
            val intent = Intent(this, TripDetailsActivity::class.java)
            startActivity(intent)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_create -> {
                    Toast.makeText(this, "You're already on Trips", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_notifications -> Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}
