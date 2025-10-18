package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExpenseSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_summary)

        val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Highlight Expenses tab
        bottomNav.selectedItemId = R.id.nav_create

        // Add expense button
        btnAddExpense.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        // Tabs (top navigation)
        findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            Toast.makeText(this, "Participants tab clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.tabPolls).setOnClickListener {
            Toast.makeText(this, "Polls tab clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            Toast.makeText(this, "You’re already on Expenses", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.tabDocs).setOnClickListener {
            Toast.makeText(this, "Docs tab clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            val intent = Intent(this, ItineraryActivity::class.java)
            startActivity(intent)
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
