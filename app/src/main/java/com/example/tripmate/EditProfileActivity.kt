package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val tvBack = findViewById<TextView>(R.id.tvBack)
        val imgProfilePhoto = findViewById<ImageView>(R.id.imgProfilePhoto)
        val tvUploadPhoto = findViewById<TextView>(R.id.tvUploadPhoto)
        val etName = findViewById<EditText>(R.id.etName)
        val etBio = findViewById<EditText>(R.id.etBio)
        val btnSaveProfile = findViewById<Button>(R.id.btnSaveProfile)

        // Back to Profile Page
        tvBack.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }


        // Confirmation dialog before saving
        btnSaveProfile.setOnClickListener {
            val name = etName.text.toString().trim()
            val bio = etBio.text.toString().trim()

            if (name.isEmpty() || bio.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show confirmation dialog
            AlertDialog.Builder(this)
                .setTitle("Save Changes")
                .setMessage("Are you sure you want to save your profile changes?")
                .setPositiveButton("Yes") { dialog, _ ->
                    Toast.makeText(this, "Profile saved!\nName: $name\nBio: $bio", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                    //  navigate back to ProfileActivity after saving
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(this, "Save canceled.", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }
}
