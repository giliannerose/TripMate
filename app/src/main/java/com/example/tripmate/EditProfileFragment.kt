package com.example.tripmate.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var imgProfile: ImageView
    private val PICK_IMAGE_REQUEST = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvBack = view.findViewById<TextView>(R.id.tvBack)
        val tvUploadPhoto = view.findViewById<TextView>(R.id.tvUploadPhoto)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etBio = view.findViewById<EditText>(R.id.etBio)
        val btnSaveProfile = view.findViewById<Button>(R.id.btnSaveProfile)
        imgProfile = view.findViewById(R.id.imgProfile)

        // Back
        tvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Profile photo picker (system intent stays)
        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Save profile
        btnSaveProfile.setOnClickListener {
            val name = etName.text.toString().trim()
            val bio = etBio.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Name is required"
                etName.requestFocus()
                return@setOnClickListener
            }

            if (bio.isEmpty()) {
                etBio.error = "Bio is required"
                etBio.requestFocus()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Save Changes")
                .setMessage("Are you sure you want to save your profile changes?")
                .setPositiveButton("Yes") { dialog, _ ->
                    Toast.makeText(
                        requireContext(),
                        "Profile saved!\nName: $name\nBio: $bio",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                    findNavController().popBackStack()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Save canceled.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {
            val imageUri: Uri? = data.data
            imgProfile.setImageURI(imageUri)
            Toast.makeText(
                requireContext(),
                "Profile photo updated!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
