package com.example.tripmate

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class CreateTripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

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

        // Save button
        btnSaveTrip.setOnClickListener {
            Toast.makeText(this, "Trip saved successfully!", Toast.LENGTH_SHORT).show()
        }

        // Invite members button
        btnInviteMembers.setOnClickListener {
            Toast.makeText(this, "Invite members clicked!", Toast.LENGTH_SHORT).show()
        }

        // Bottom nav
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
