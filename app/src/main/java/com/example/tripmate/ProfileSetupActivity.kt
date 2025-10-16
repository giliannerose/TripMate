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
                imgProfile.setImageURI(imageUri)
            }
        }

        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }

        // Save changes
        btnSaveChanges.setOnClickListener {
            val name = etName.text.toString()
            val bio = etBio.text.toString()
            val location = etLocation.text.toString()

            if (name.isEmpty() || bio.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                // Go to Welcome Dialog
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
