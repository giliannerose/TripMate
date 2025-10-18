package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

        // Upload photo (placeholder toast)
        tvUploadPhoto.setOnClickListener {
            Toast.makeText(this, "Upload feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        imgProfilePhoto.setOnClickListener {
            Toast.makeText(this, "Profile photo clicked", Toast.LENGTH_SHORT).show()
        }

        // Save changes (no backend yet)
        btnSaveProfile.setOnClickListener {
            val name = etName.text.toString()
            val bio = etBio.text.toString()
            Toast.makeText(this, "Profile saved!\nName: $name\nBio: $bio", Toast.LENGTH_SHORT).show()
        }
    }
}
