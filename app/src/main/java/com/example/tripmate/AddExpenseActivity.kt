package com.example.tripmate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val etDate = findViewById<EditText>(R.id.etDate)
        val btnPickDate = findViewById<ImageView>(R.id.btnPickDate)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnViewSummary = findViewById<Button>(R.id.btnViewSummary)
        val btnUpload = findViewById<Button>(R.id.btnUpload)
        val tvFileChosen = findViewById<TextView>(R.id.tvFileChosen)
        val spCategory = findViewById<Spinner>(R.id.spCategory)
        val spPaidBy = findViewById<Spinner>(R.id.spPaidBy)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Dropdowns
        val categories = arrayOf("Food", "Transport", "Accommodation", "Activity", "Other")
        spCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        val members = arrayOf("Jane Doe", "Alice", "Bob", "You")
        spPaidBy.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, members)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)
        //----------

        // Date Picker
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    etDate.setText("$day/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Upload placeholder
        btnUpload.setOnClickListener {
            tvFileChosen.text = "receipt.jpg"
            Toast.makeText(this, "Simulated upload complete", Toast.LENGTH_SHORT).show()
        }

        // Buttons
        btnSave.setOnClickListener {
            Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show()
        }

        btnViewSummary.setOnClickListener {
            Toast.makeText(this, "View Summary clicked!", Toast.LENGTH_SHORT).show()
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
