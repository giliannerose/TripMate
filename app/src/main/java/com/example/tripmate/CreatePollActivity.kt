package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.EditText


class CreatePollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)

        val etPollQuestion = findViewById<EditText>(R.id.etPollQuestion)
        val etOption1 = findViewById<EditText>(R.id.etOption1)
        val etOption2 = findViewById<EditText>(R.id.etOption2)
        val etOption3 = findViewById<EditText>(R.id.etOption3)
        val etOption4 = findViewById<EditText>(R.id.etOption4)


        val btnCreatePoll = findViewById<Button>(R.id.btnCreatePoll)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)
        //----------

        btnCreatePoll.setOnClickListener {

            val question = etPollQuestion.text.toString().trim()
            val option1 = etOption1.text.toString().trim()
            val option2 = etOption2.text.toString().trim()


            etPollQuestion.error = null
            etOption1.error = null
            etOption2.error = null

            // Question validation
            if (question.isEmpty()) {
                etPollQuestion.error = "Poll question is required"
                etPollQuestion.requestFocus()
                return@setOnClickListener
            }

            // Option validation
            if (option1.isEmpty()) {
                etOption1.error = "At least two options are required"
                etOption1.requestFocus()
                return@setOnClickListener
            }

            if (option2.isEmpty()) {
                etOption2.error = "At least two options are required"
                etOption2.requestFocus()
                return@setOnClickListener
            }

            // If valid
            Toast.makeText(this, "Poll created successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, VoteActivity::class.java))
        }


        // Top Tabs navigation
        findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            val intent = Intent(this, TripDetailsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.tabPolls).setOnClickListener {
            Toast.makeText(this, "You're already on Polls", Toast.LENGTH_SHORT).show()
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
