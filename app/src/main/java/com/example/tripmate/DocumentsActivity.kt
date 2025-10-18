package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Handler


class DocumentsActivity : AppCompatActivity() {

    private lateinit var btnUpload: Button
    private val PICK_FILE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

        val btnUpload = findViewById<Button>(R.id.btnUpload)
        val tabParticipants = findViewById<Button>(R.id.tabParticipants)
        val tabPolls = findViewById<Button>(R.id.tabPolls)
        val tabExpenses = findViewById<Button>(R.id.tabExpenses)
        val tabDocs = findViewById<Button>(R.id.tabDocs)
        val tabItinerary = findViewById<Button>(R.id.tabItinerary)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)
        //----------

        // Upload Button
        btnUpload.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(Intent.createChooser(intent, "Select a file to upload"), PICK_FILE_REQUEST)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            val fileUri: Uri? = data?.data

            if (fileUri != null) {
                // Show mock uploading progress
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Uploading file...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                // Simulate upload delay (2 seconds)
                Handler().postDelayed({
                    progressDialog.dismiss()
                    Toast.makeText(this, "File uploaded successfully!", Toast.LENGTH_LONG).show()
                }, 2000)
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
