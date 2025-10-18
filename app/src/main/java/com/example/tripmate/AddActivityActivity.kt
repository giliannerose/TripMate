package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddActivityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activity)

        val etDate = findViewById<EditText>(R.id.etDate)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etNotes = findViewById<EditText>(R.id.etNotes)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            val date = etDate.text.toString()
            val time = etTime.text.toString()
            val title = etTitle.text.toString()
            val notes = etNotes.text.toString()

            if (date.isEmpty() || time.isEmpty() || title.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            } else {
                // For now, just show confirmation
                Toast.makeText(this, "Activity added successfully!", Toast.LENGTH_SHORT).show()

                // Return to ItineraryActivity
                val intent = Intent(this, ItineraryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        btnCancel.setOnClickListener {
            // Go back to itinerary page without saving
            finish()
        }
    }
}
