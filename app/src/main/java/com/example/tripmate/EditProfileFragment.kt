package com.example.tripmate.ui.profile


import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import androidx.lifecycle.ViewModelProvider
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tripmate.databinding.FragmentEditProfileBinding
import com.example.tripmate.ui.user.UserViewModel
import com.example.tripmate.data.model.UserEntity

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private var currentUser: UserEntity? = null
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.imgProfile.setImageURI(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditProfileBinding.bind(view)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val userId = 1 // TODO: replace with real logged-in user

        userViewModel.getUserById(userId)
            .observe(viewLifecycleOwner) { user ->

                user?.let {
                    currentUser = it
                    binding.etName.setText(it.name)
                    binding.etBio.setText(it.bio)

                    it.profileImageUri?.let { uri ->
                        binding.imgProfile.setImageURI(Uri.parse(uri))
                    }
                }
            }

        binding.tvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSaveProfile.setOnClickListener {
            saveProfile(userId)
        }
    }

    private fun saveProfile(userId: Int) {

        val name = binding.etName.text.toString().trim()
        val bio = binding.etBio.text.toString().trim()

        if (name.isEmpty()) {
            binding.etName.error = "Name required"
            return
        }

        if (bio.isEmpty()) {
            binding.etBio.error = "Bio required"
            return
        }

        val updatedUser = currentUser?.copy(
            name = name,
            bio = bio,
            profileImageUri = selectedImageUri?.toString()
                ?: currentUser?.profileImageUri
        )

        updatedUser?.let {
            userViewModel.update(it)
            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

}
