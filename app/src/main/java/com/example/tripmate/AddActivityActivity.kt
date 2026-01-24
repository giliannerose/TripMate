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

            val date = etDate.text.toString().trim()
            val time = etTime.text.toString().trim()
            val title = etTitle.text.toString().trim()
            val notes = etNotes.text.toString().trim()


            etDate.error = null
            etTime.error = null
            etTitle.error = null

            if (date.isEmpty()) {
                etDate.error = "Date is required"
                etDate.requestFocus()
                return@setOnClickListener
            }

            if (time.isEmpty()) {
                etTime.error = "Time is required"
                etTime.requestFocus()
                return@setOnClickListener
            }

            if (title.isEmpty()) {
                etTitle.error = "Title is required"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            // All inputs valid
            Toast.makeText(this, "Activity added successfully!", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, ItineraryActivity::class.java))
            finish()
        }


        btnCancel.setOnClickListener {
            // Go back to itinerary page without saving
            finish()
        }
    }
}
