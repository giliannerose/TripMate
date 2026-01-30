package com.example.tripmate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController

class ProfileSetupFragment : Fragment(R.layout.fragment_profile_setup) {

    private lateinit var imgProfile: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgProfile = view.findViewById(R.id.imgProfile)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etBio = view.findViewById<EditText>(R.id.etBio)
        val etLocation = view.findViewById<EditText>(R.id.etLocation)
        val btnSaveChanges = view.findViewById<Button>(R.id.btnSaveChanges)
        val btnSkip = view.findViewById<Button>(R.id.btnSkip)

        val imagePicker = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null) {
                    imgProfile.setImageURI(imageUri)
                    Toast.makeText(requireContext(), "Profile photo added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }

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
                Toast.makeText(requireContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show()

                view.findNavController()
                    .navigate(R.id.action_profileSetupFragment_to_loginSuccessFragment)
            }
        }

        btnSkip.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_profileSetupFragment_to_loginSuccessFragment)
        }
    }

}