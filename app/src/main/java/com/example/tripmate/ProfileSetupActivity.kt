package com.example.tripmate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setup)

        imgProfile = findViewById(R.id.imgProfile)
        val etName = findViewById<EditText>(R.id.etName)
        val etBio = findViewById<EditText>(R.id.etBio)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val btnSaveChanges = findViewById<Button>(R.id.btnSaveChanges)
        val btnSkip = findViewById<Button>(R.id.btnSkip)

        // Select image from gallery
        val imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data

                if (imageUri != null) {
                    imgProfile.setImageURI(imageUri)
                    Toast.makeText(this, "Profile photo added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
                }
            }

        }

        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }

        // Save changes
        btnSaveChanges.setOnClickListener {
            val name = etName.text.toString().trim()
            val bio = etBio.text.toString().trim()
            val location = etLocation.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                etName.error = "Name is required"
                isValid = false
            }

            if (bio.isEmpty()) {
                etBio.error = "Bio is required"
                isValid = false
            }

            if (location.isEmpty()) {
                etLocation.error = "Location is required"
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginSuccessActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        // Skip button
        btnSkip.setOnClickListener {
            val intent = Intent(this, LoginSuccessActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
