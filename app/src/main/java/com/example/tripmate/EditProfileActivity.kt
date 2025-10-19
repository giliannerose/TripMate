package com.example.tripmate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class EditProfileActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val tvBack = findViewById<TextView>(R.id.tvBack)
        val tvUploadPhoto = findViewById<TextView>(R.id.tvUploadPhoto)
        val etName = findViewById<EditText>(R.id.etName)
        val etBio = findViewById<EditText>(R.id.etBio)
        val btnSaveProfile = findViewById<Button>(R.id.btnSaveProfile)
        imgProfile = findViewById(R.id.imgProfile)

        // Back to Profile Page
        tvBack.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        // ptofile photo clickable
        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imgProfile.setImageURI(imageUri)
            Toast.makeText(this, "Profile photo updated!", Toast.LENGTH_SHORT).show()
        }
    }
}
