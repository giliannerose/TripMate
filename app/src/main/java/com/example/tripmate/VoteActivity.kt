package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        val btnSubmitVote1 = findViewById<Button>(R.id.btnSubmitVote1)
        val btnSubmitVote2 = findViewById<Button>(R.id.btnSubmitVote2)

        val tabParticipants = findViewById<Button>(R.id.tabParticipants)
        val tabPolls = findViewById<Button>(R.id.tabPolls)
        val tabExpenses = findViewById<Button>(R.id.tabExpenses)
        val tabDocs = findViewById<Button>(R.id.tabDocs)
        val tabItinerary = findViewById<Button>(R.id.tabItinerary)

        btnSubmitVote1.setOnClickListener {
            Toast.makeText(this, "Vote submitted for first poll!", Toast.LENGTH_SHORT).show()
            it.isEnabled = false
            it.alpha = 0.5f
        }

        btnSubmitVote2.setOnClickListener {
            Toast.makeText(this, "Vote submitted for second poll!", Toast.LENGTH_SHORT).show()
            it.isEnabled = false
            it.alpha = 0.5f
        }

        // Top Navigation tabs
        tabParticipants.setOnClickListener {
            startActivity(Intent(this, TripDetailsActivity::class.java))
        }
        tabPolls.setOnClickListener {
            Toast.makeText(this, "You're already on Polls", Toast.LENGTH_SHORT).show()
        }
        tabExpenses.setOnClickListener {
            startActivity(Intent(this, ExpenseSummaryActivity::class.java))
        }
        tabDocs.setOnClickListener {
            startActivity(Intent(this, DocumentsActivity::class.java))
        }
        tabItinerary.setOnClickListener {
            startActivity(Intent(this, ItineraryActivity::class.java))
        }
    }
}
