package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DocumentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

        val btnUpload = findViewById<Button>(R.id.btnUpload)
        val tabParticipants = findViewById<Button>(R.id.tabParticipants)
        val tabPolls = findViewById<Button>(R.id.tabPolls)
        val tabExpenses = findViewById<Button>(R.id.tabExpenses)
        val tabDocs = findViewById<Button>(R.id.tabDocs)
        val tabItinerary = findViewById<Button>(R.id.tabItinerary)

        // Upload Button (temporary Toast)
        btnUpload.setOnClickListener {
            Toast.makeText(this, "Upload feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Tab navigation
        tabParticipants.setOnClickListener {
            startActivity(Intent(this, TripDetailsActivity::class.java))
        }
        tabPolls.setOnClickListener {
            startActivity(Intent(this, CreatePollActivity::class.java))
        }
        tabExpenses.setOnClickListener {
            startActivity(Intent(this, ExpenseSummaryActivity::class.java))
        }
        tabDocs.setOnClickListener {
            Toast.makeText(this, "You're already on Docs", Toast.LENGTH_SHORT).show()
        }
        tabItinerary.setOnClickListener {
            startActivity(Intent(this, ItineraryActivity::class.java))
        }
    }
}
