package com.example.tripmate.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.remote.FirebaseStorageManager
import com.example.tripmate.data.utils.SessionManager
import com.example.tripmate.databinding.FragmentEditProfileBinding
import com.example.tripmate.ui.user.UserViewModel

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sessionManager: SessionManager
    private val storageManager = FirebaseStorageManager()
    private var currentUser: UserEntity? = null
    private var selectedImageUri: Uri? = null


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.imgProfile.setImageURI(it)
                Log.d("TRIPMATE_DEBUG", "Image selected: $it")
            }
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditProfileBinding.bind(view)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sessionManager = SessionManager(requireContext())


        binding.imgProfile.setOnClickListener {
            Log.d("TRIPMATE_DEBUG", "Image clicked!")
            pickImageLauncher.launch("image/*")
        }


        binding.tvUploadPhoto.setOnClickListener {
            Log.d("TRIPMATE_DEBUG", "Text clicked!")
            pickImageLauncher.launch("image/*")
        }

        // Retrieve real ID from Session
        val userId = sessionManager.getUserId()


        if (userId != SessionManager.NO_USER) {
            userViewModel.getUserById(userId).observe(viewLifecycleOwner) { user ->
                user?.let {
                    currentUser = it
                    populateFields(it)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Error: Session expired", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.tvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun populateFields(user: UserEntity) {
        binding.etName.setText(user.name)
        binding.etBio.setText(user.bio)
        binding.etAge.setText(if (user.age != 0) user.age.toString() else "")

        setSpinnerSelection(binding.spGender, user.gender)
        setSpinnerSelection(binding.spRegion, user.region)

        user.profileImageUri?.let { uriString ->
            try {
                binding.imgProfile.setImageURI(Uri.parse(uriString))
            } catch (e: Exception) {
                binding.imgProfile.setImageResource(R.drawable.ic_default_avatar)
            }
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val bio = binding.etBio.text.toString().trim()
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
        val gender = binding.spGender.selectedItem.toString()
        val region = binding.spRegion.selectedItem.toString()

        if (name.isEmpty()) {
            binding.etName.error = "Name required"
            return
        }

        binding.btnSaveProfile.isEnabled = false

        if (selectedImageUri != null) {
            Toast.makeText(requireContext(), "Attempting Cloud Upload...", Toast.LENGTH_SHORT).show()

            // TRY FIREBASE FIRST
            storageManager.uploadFile(selectedImageUri!!, "profiles", requireContext()) { success, result ->
                if (success) {
                    // SUCCESS: Save the HTTPS URL
                    Log.d("TRIPMATE_DEBUG", "Cloud Upload Success: $result")
                    updateUserInDatabase(name, bio, age, gender, region, result)
                } else {
                    // FAILURE: Falling back to Local Storage
                    Log.w("TRIPMATE_DEBUG", "Cloud Failed ($result). Saving locally instead.")
                    Toast.makeText(requireContext(), "Cloud limited. Saving to device...", Toast.LENGTH_LONG).show()

                    val localPath = saveImageLocally(selectedImageUri!!)
                    updateUserInDatabase(name, bio, age, gender, region, localPath)
                }
            }
        } else {
            // No new image selected, just update text fields
            updateUserInDatabase(name, bio, age, gender, region, currentUser?.profileImageUri)
        }
    }

    private fun saveImageLocally(uri: Uri): String? {
        return try {

            val inputStream = requireContext().contentResolver.openInputStream(uri)


            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = java.io.File(requireContext().filesDir, fileName)


            val outputStream = java.io.FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            Log.d("TRIPMATE_DEBUG", "Image saved locally at: ${file.absolutePath}")
            file.absolutePath
        } catch (e: Exception) {
            Log.e("TRIPMATE_DEBUG", "Failed to save image locally: ${e.message}")
            null
        }
    }

    private fun updateUserInDatabase(
        name: String,
        bio: String,
        age: Int,
        gender: String,
        region: String,
        imageUri: String?
    ) {
        val updatedUser = currentUser?.copy(
            name = name,
            bio = bio,
            age = age,
            gender = if (gender == "Select Gender") "" else gender,
            region = if (region == "Select Region") "" else region,
            profileImageUri = imageUri
        )

        updatedUser?.let {
            userViewModel.update(it)
            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } ?: run {
            binding.btnSaveProfile.isEnabled = true
        }
    }

    private fun setSpinnerSelection(spinner: Spinner, value: String) {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == value) {
                spinner.setSelection(i)
                break
            }
        }
    }
}