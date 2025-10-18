package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import androidx.cardview.widget.CardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ExpenseSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_summary)

        val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val expenseCard = findViewById<CardView>(R.id.expenseCard)
        val btnDeleteExpense = findViewById<Button>(R.id.btnDeleteExpense)
        val expenseCard2 = findViewById<CardView>(R.id.expenseCard2)
        val btnMark2 = findViewById<Button>(R.id.btnMarkSettled2)
        val btnDelete2 = findViewById<Button>(R.id.btnDeleteExpense2)



        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)
        //----------

        // Add expense button
        btnAddExpense.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
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
            Toast.makeText(this, "You're already on Expenses", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.tabDocs).setOnClickListener {
            val intent = Intent(this, DocumentsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            val intent = Intent(this, ItineraryActivity::class.java)
            startActivity(intent)
        }

        val btnMarkSettled = findViewById<Button>(R.id.btnMarkSettled)

        btnMarkSettled.setOnClickListener {
            // Change color to gray
            btnMarkSettled.setBackgroundTintList(
                getColorStateList(android.R.color.darker_gray)
            )

            // Change text to "Settled"
            btnMarkSettled.text = "Settled"

            // Disable the button
            btnMarkSettled.isEnabled = false


            Toast.makeText(this, "Expense marked as settled", Toast.LENGTH_SHORT).show()
        }

        // Delete button
        btnDeleteExpense.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Yes") { dialog, _ ->
                    expenseCard.visibility = View.GONE
                    Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // 2nd expense
        btnMark2.setOnClickListener {
            btnMark2.setBackgroundTintList(getColorStateList(android.R.color.darker_gray))
            btnMark2.text = "Settled"
            btnMark2.isEnabled = false
            Toast.makeText(this, "Expenses settled", Toast.LENGTH_SHORT).show()
        }

        btnDelete2.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Yes") { dialog, _ ->
                    expenseCard2.visibility = View.GONE
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
                R.id.nav_create -> {
                    startActivity(Intent(this, MyTripsActivity::class.java))
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
            }
            overridePendingTransition(0, 0)
            true
        }
    }
}
