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
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ItineraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerary)

        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipeRefresh.setOnRefreshListener {
            Toast.makeText(this, "Itinerary refreshed", Toast.LENGTH_SHORT).show()
            swipeRefresh.isRefreshing = false
        }


        val btnAddActivity = findViewById<Button>(R.id.btnAddActivity)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val editButton = findViewById<Button>(R.id.btnEditActivity)
        val includedLayout = findViewById<View>(R.id.include_day1_activity)
        val titleView = includedLayout.findViewById<TextView>(R.id.tvActivityTitle)
        val timeView = includedLayout.findViewById<TextView>(R.id.tvActivityTime)

//add Activity button

        btnAddActivity.setOnClickListener {
            val intent = Intent(this, AddActivityActivity::class.java)
            startActivity(intent)
        }



        // day 1
        val day1Layout = findViewById<View>(R.id.include_day1_activity)
        val day1Title = day1Layout.findViewById<TextView>(R.id.tvActivityTitle)
        val day1Time = day1Layout.findViewById<TextView>(R.id.tvActivityTime)
        val day1Edit = day1Layout.findViewById<Button>(R.id.btnEditActivity)

        // day 2
        val day2Layout = findViewById<View>(R.id.include_day2_activity)
        val day2Title = day2Layout.findViewById<TextView>(R.id.tvActivityTitle)
        val day2Time = day2Layout.findViewById<TextView>(R.id.tvActivityTime)
        val day2Edit = day2Layout.findViewById<Button>(R.id.btnEditActivity)

        // for delete
        val day1Delete = day1Layout.findViewById<Button>(R.id.btnDeleteActivity)
        val day2Delete = day2Layout.findViewById<Button>(R.id.btnDeleteActivity)


        // 🔧 Day 1 edit button
        day1Edit.setOnClickListener {
            showEditDialog(day1Title, day1Time)
        }

        // 🔧 Day 2 edit button
        day2Edit.setOnClickListener {
            showEditDialog(day2Title, day2Time)
        }

        //  Delete buttons
        day1Delete.setOnClickListener { showDeleteDialog(day1Layout, "Day 1 activity deleted!") }
        day2Delete.setOnClickListener { showDeleteDialog(day2Layout, "Day 2 activity deleted!") }






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

    private fun showEditDialog(titleView: TextView, timeView: TextView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_activity, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etTime = dialogView.findViewById<EditText>(R.id.etTime)

        etTitle.setText(titleView.text.toString())
        etTime.setText(timeView.text.toString())

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Activity")
            .setView(dialogView)
            .setPositiveButton("Save", null)   // override click
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                var isValid = true

                if (etTitle.text.isBlank()) {
                    etTitle.error = "Title is required"
                    isValid = false
                }

                if (etTime.text.isBlank()) {
                    etTime.error = "Time is required"
                    isValid = false
                }

                if (isValid) {
                    titleView.text = etTitle.text.toString()
                    timeView.text = etTime.text.toString()
                    Toast.makeText(this, "Activity updated!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()



    }

    private fun showDeleteDialog(layoutToRemove: View, message: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Activity")
            .setMessage("Are you sure you want to delete this activity?")
            .setPositiveButton("Delete") { _, _ ->
                layoutToRemove.visibility = View.GONE
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}

