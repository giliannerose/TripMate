package com.example.tripmate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.utils.SessionManager
import com.example.tripmate.ui.user.UserViewModel
import kotlinx.coroutines.launch

class ProfileSetupFragment : Fragment(R.layout.fragment_profile_setup) {

    private lateinit var imgProfile: ImageView
    private lateinit var userViewModel: UserViewModel
    private lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                selectedImageUri = imageUri
                imgProfile.setImageURI(imageUri)
                Toast.makeText(requireContext(), "Profile photo added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        imgProfile = view.findViewById(R.id.imgProfile)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etBio = view.findViewById<EditText>(R.id.etBio)
        val etLocation = view.findViewById<EditText>(R.id.etLocation)
        val btnSaveChanges = view.findViewById<Button>(R.id.btnSaveChanges)
        val btnSkip = view.findViewById<Button>(R.id.btnSkip)

        // Pre-fill name from session
        etName.setText(sessionManager.getUserName())

        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }

        btnSaveChanges.setOnClickListener {
            val name = etName.text.toString().trim()
            val bio = etBio.text.toString().trim()
            val location = etLocation.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Name is required"
                return@setOnClickListener
            }

            val email = sessionManager.getUserEmail()
            if (email != null) {
                lifecycleScope.launch {
                    val existingUser = userViewModel.getUserByEmail(email)
                    if (existingUser != null) {
                        val updatedUser = existingUser.copy(
                            name = name,
                            bio = bio,
                            region = location,
                            profileImageUri = selectedImageUri?.toString() ?: existingUser.profileImageUri
                        )
                        userViewModel.update(updatedUser)
                        Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_profileSetupFragment_to_loginSuccessFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error: User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_profileSetupFragment_to_loginSuccessFragment)
        }
    }
}