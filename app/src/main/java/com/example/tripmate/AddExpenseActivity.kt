package com.example.tripmate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etNotes = findViewById<EditText>(R.id.etNotes)

        val cbAlice = findViewById<CheckBox>(R.id.cbAlice)
        val cbBob = findViewById<CheckBox>(R.id.cbBob)
        val cbJane = findViewById<CheckBox>(R.id.cbJane)

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

        // Save Button
        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val amountText = etAmount.text.toString().trim()
            val date = etDate.text.toString().trim()

            etTitle.error = null
            etAmount.error = null
            etDate.error = null

            if (title.isEmpty()) {
                etTitle.error = "Expense title is required"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            if (amountText.isEmpty()) {
                etAmount.error = "Amount is required"
                etAmount.requestFocus()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                etAmount.error = "Enter a valid amount"
                etAmount.requestFocus()
                return@setOnClickListener
            }


            if (date.isEmpty()) {
                etDate.error = "Date is required"
                etDate.requestFocus()
                return@setOnClickListener
            }


            if (!cbAlice.isChecked && !cbBob.isChecked && !cbJane.isChecked) {
                Toast.makeText(this, "Select at least one person to split with", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            MaterialAlertDialogBuilder(this)
                .setTitle("Save Expense")
                .setMessage("Do you want to save this expense and go to the summary?")
                .setPositiveButton("Yes") { dialog, _ ->
                    Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ExpenseSummaryActivity::class.java))
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
        }


        //  view summary button
        btnViewSummary.setOnClickListener {
            val intent = Intent(this, ExpenseSummaryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
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
            Toast.makeText(this, "Expenses", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.tabDocs).setOnClickListener {
            val intent = Intent(this, DocumentsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            val intent = Intent(this, ItineraryActivity::class.java)
            startActivity(intent)
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
