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

        val btnInvite = findViewById<Button>(R.id.btnInvite)   // INVITE BUTTON
        val btnExpense = findViewById<Button>(R.id.btnExpense) // expense

        bottomNav.selectedItemId = R.id.nav_create

        btnCreateTrip.setOnClickListener {
            val intent = Intent(this, CreateTripActivity::class.java)
            startActivity(intent)
        }

        cardSiargao.setOnClickListener {
            val intent = Intent(this, TripDetailsActivity::class.java)
            startActivity(intent)
        }


        btnInvite.setOnClickListener {
            val intent = Intent(this, InviteMembersActivity::class.java)
            startActivity(intent)
        }

        btnExpense.setOnClickListener {
            val intent = Intent(this, ExpenseSummaryActivity::class.java)
            startActivity(intent)
        }


        val btnDeleteNewTrip = findViewById<Button>(R.id.btnDeleteNewTrip)
        val btnDeleteSiargao = findViewById<Button>(R.id.btnDeleteSiargao)
        val btnDeleteMadrid = findViewById<Button>(R.id.btnDeleteMadrid)

        btnDeleteNewTrip.setOnClickListener {
            showDeleteConfirmation("New Trip")
        }

        btnDeleteSiargao.setOnClickListener {
            showDeleteConfirmation("Siargao Weekend")
        }

        btnDeleteMadrid.setOnClickListener {
            showDeleteConfirmation("Madrid x Barcelona")
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
                R.id.nav_notifications -> {
                    val intent = Intent(this, NotificationsActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    private fun showDeleteConfirmation(tripName: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Trip?")
        builder.setMessage("This will permanently delete the trip \"$tripName\" and its itineraries. This action cannot be undone.")

        builder.setPositiveButton("Delete") { dialog, _ ->
            Toast.makeText(this, "$tripName deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        // Optional: customize button colors
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(android.R.color.holo_red_dark))
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(android.R.color.darker_gray))
    }

}
