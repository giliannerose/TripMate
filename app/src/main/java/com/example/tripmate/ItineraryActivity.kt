package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.EditText
import android.widget.TextView
import android.view.View

class ItineraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerary)

        val btnAddActivity = findViewById<Button>(R.id.btnAddActivity)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val editButton = findViewById<Button>(R.id.btnEditActivity)
        val includedLayout = findViewById<View>(R.id.include_day1_activity)
        val titleView = includedLayout.findViewById<TextView>(R.id.tvActivityTitle)
        val timeView = includedLayout.findViewById<TextView>(R.id.tvActivityTime)



        btnAddActivity.setOnClickListener {
            Toast.makeText(this, "Add new activity clicked!", Toast.LENGTH_SHORT).show()
        }

        editButton.setOnClickListener {
            // Show a simple pop-up dialog to edit details
            val dialogView = layoutInflater.inflate(R.layout.dialog_edit_activity, null)
            val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
            val etTime = dialogView.findViewById<EditText>(R.id.etTime)

            etTitle.setText(titleView.text.toString())
            etTime.setText(timeView.text.toString())

            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Edit Activity")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    titleView.text = etTitle.text.toString()
                    timeView.text = etTime.text.toString()
                    Toast.makeText(this, "Activity updated!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
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
