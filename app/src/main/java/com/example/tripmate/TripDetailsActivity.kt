package com.example.tripmate

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import android.widget.*


class TripDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        val tabParticipants = findViewById<Button>(R.id.tabParticipants)
        val tabPolls = findViewById<Button>(R.id.tabPolls)
        val tabExpenses = findViewById<Button>(R.id.tabExpenses)
        val tabDocs = findViewById<Button>(R.id.tabDocs)
        val tabItinerary = findViewById<Button>(R.id.tabItinerary)
        val btnAddParticipant = findViewById<Button>(R.id.btnAddParticipant)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val participantsContainer = findViewById<LinearLayout>(R.id.participantsContainer)


        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)
        //----------

        // 🔹 Add Edit/Delete actions to each card
        for (i in 0 until participantsContainer.childCount) {
            val card = participantsContainer.getChildAt(i)
            val editIcon = card.findViewById<ImageView>(R.id.ic_edit)
            val deleteIcon = card.findViewById<ImageView>(R.id.ic_delete)
            val nameText = card.findViewById<TextView>(R.id.tvName)
            val emailText = card.findViewById<TextView>(R.id.tvEmail)

            // Edit button click
            editIcon?.setOnClickListener {
                val input = EditText(this)
                input.setText(nameText?.text)

                AlertDialog.Builder(this)
                    .setTitle("Edit Participant")
                    .setMessage("Update name for this participant:")
                    .setView(input)
                    .setPositiveButton("Save") { _, _ ->
                        nameText?.text = input.text.toString()
                        Toast.makeText(this, "Name updated!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            // Delete button click
            deleteIcon?.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Remove Participant")
                    .setMessage("Are you sure you want to delete ${nameText?.text}?")
                    .setPositiveButton("Yes") { _, _ ->
                        participantsContainer.removeView(card)
                        Toast.makeText(this, "Participant deleted!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }

        // Top tab navigation
        tabParticipants.setOnClickListener { Toast.makeText(this, "You're in Participants", Toast.LENGTH_SHORT).show() }

        tabPolls.setOnClickListener {
            val intent = Intent(this, CreatePollActivity::class.java)
            startActivity(intent)
        }

        tabExpenses.setOnClickListener {
            val intent = Intent(this, ExpenseSummaryActivity::class.java)
            startActivity(intent)
        }

        tabDocs.setOnClickListener {
            val intent = Intent(this, DocumentsActivity::class.java)
            startActivity(intent)
        }

        tabItinerary.setOnClickListener {
            val intent = Intent(this, ItineraryActivity::class.java)
            startActivity(intent)
        }

        //add participant button
        btnAddParticipant.setOnClickListener {
            val intent = Intent(this, InviteMembersActivity::class.java)
            startActivity(intent)
        }

        // Bottom nav interactions
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                }

                R.id.nav_create -> {
                    val intent = Intent(this, MyTripsActivity::class.java)
                    startActivity(intent)
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
}