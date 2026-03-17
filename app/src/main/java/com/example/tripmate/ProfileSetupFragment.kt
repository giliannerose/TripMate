package com.example.tripmate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.remote.FirebaseStorageManager
import com.example.tripmate.data.utils.SessionManager
import com.example.tripmate.ui.user.UserViewModel
import kotlinx.coroutines.launch

class ProfileSetupFragment : Fragment(R.layout.fragment_profile_setup) {

    private lateinit var imgProfile: ImageView
    private lateinit var userViewModel: UserViewModel
    private lateinit var sessionManager: SessionManager
    private val storageManager = FirebaseStorageManager()
    private var selectedFileUri: Uri? = null

    // Universal File Picker Callback
    private val filePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFileUri = uri
                // Preview the image
                imgProfile.setImageURI(uri)
                Toast.makeText(requireContext(), "File attached!", Toast.LENGTH_SHORT).show()
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

        etName.setText(sessionManager.getUserName())

        // Use GET_CONTENT to allow picking from Gallery, File Manager, or Downloads
        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*" // Allow images and documents
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            filePicker.launch(intent)
        }

        btnSaveChanges.setOnClickListener {
            val name = etName.text.toString().trim()
            val bio = etBio.text.toString().trim()
            val location = etLocation.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Name is required"
                return@setOnClickListener
            }

            val email = sessionManager.getUserEmail() ?: return@setOnClickListener

            lifecycleScope.launch {
                val user = userViewModel.getUserByEmail(email)
                if (user != null) {
                    if (selectedFileUri != null) {
                        // START FIREBASE UPLOAD
                        Toast.makeText(requireContext(), "Uploading to cloud...", Toast.LENGTH_SHORT).show()
                        storageManager.uploadFile(selectedFileUri!!, "profiles", requireContext()) { success, url ->
                            if (success) {
                                saveUserData(user, name, bio, location, url)
                            } else {
                                Log.e("STORAGE_ERROR", url ?: "Unknown error")
                                Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // No new file, just save text changes
                        saveUserData(user, name, bio, location, user.profileImageUri)
                    }
                }
            }
        }

        btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_profileSetupFragment_to_loginSuccessFragment)
        }
    }

    private fun saveUserData(user: UserEntity, name: String, bio: String, loc: String, photoUrl: String?) {
        val updatedUser = user.copy(
            name = name,
            bio = bio,
            region = loc,
            profileImageUri = photoUrl
        )
        userViewModel.update(updatedUser)
        Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_profileSetupFragment_to_loginSuccessFragment)
    }
}