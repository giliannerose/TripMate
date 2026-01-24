package com.example.tripmate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class CreateTripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        val etTripName = findViewById<EditText>(R.id.etTripName)
        val etDestination = findViewById<EditText>(R.id.etDestination)


        val etStartDate = findViewById<EditText>(R.id.etStartDate)
        val etEndDate = findViewById<EditText>(R.id.etEndDate)
        val btnStartDate = findViewById<ImageView>(R.id.btnStartDate)
        val btnEndDate = findViewById<ImageView>(R.id.btnEndDate)
        val btnSaveTrip = findViewById<Button>(R.id.btnSaveTrip)
        val btnInviteMembers = findViewById<Button>(R.id.btnInviteMembers)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Date pickers
        btnStartDate.setOnClickListener {
            showDatePicker(etStartDate)
        }

        btnEndDate.setOnClickListener {
            showDatePicker(etEndDate)
        }

        btnSaveTrip.setOnClickListener {

            val tripName = etTripName.text.toString().trim()
            val destination = etDestination.text.toString().trim()
            val startDate = etStartDate.text.toString().trim()
            val endDate = etEndDate.text.toString().trim()


            etTripName.error = null
            etDestination.error = null
            etStartDate.error = null
            etEndDate.error = null

            if (tripName.isEmpty()) {
                etTripName.error = "Trip name is required"
                etTripName.requestFocus()
                return@setOnClickListener
            }

            if (destination.isEmpty()) {
                etDestination.error = "Destination is required"
                etDestination.requestFocus()
                return@setOnClickListener
            }

            if (startDate.isEmpty()) {
                etStartDate.error = "Start date is required"
                etStartDate.requestFocus()
                return@setOnClickListener
            }

            if (endDate.isEmpty()) {
                etEndDate.error = "End date is required"
                etEndDate.requestFocus()
                return@setOnClickListener
            }


            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Save Trip")
                .setMessage("Are you sure you want to save this trip?")
                .setPositiveButton("Save") { dialog, _ ->
                    Toast.makeText(this, "Trip saved successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    // navigation unchanged
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }



        btnInviteMembers.setOnClickListener {
            val intent = Intent(this, InviteMembersActivity::class.java)
            startActivity(intent)
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

    private fun showDatePicker(targetEditText: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "${dayOfMonth}/${month + 1}/$year"
                targetEditText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}
