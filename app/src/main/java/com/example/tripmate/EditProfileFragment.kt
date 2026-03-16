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
import com.example.tripmate.data.utils.SessionManager


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

        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        userViewModel.getUserById(userId)
            .observe(viewLifecycleOwner) { user ->

                user?.let {
                    currentUser = it
                    binding.etName.setText(it.name)
                    binding.etBio.setText(it.bio)
                    if (it.age != 0) {
                        binding.etAge.setText(it.age.toString())
                    } else {
                        binding.etAge.setText("")
                    }

                    setSpinnerSelection(binding.spGender, it.gender)
                    setSpinnerSelection(binding.spRegion, it.region)

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
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
        val gender = binding.spGender.selectedItem.toString()
        val region = binding.spRegion.selectedItem.toString()

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
            age = age,
            gender = if (gender == "Select Gender") "" else gender,
            region = if (region == "Select Region") "" else region,
            profileImageUri = selectedImageUri?.toString()
                ?: currentUser?.profileImageUri
        )

        updatedUser?.let {
            userViewModel.update(it)
            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
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
