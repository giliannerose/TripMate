package com.example.tripmate.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var imgProfile: ImageView
    private val PICK_IMAGE_REQUEST = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val btnSettings = view.findViewById<Button>(R.id.btnSettings)
        val btnSignOut = view.findViewById<Button>(R.id.btnSignOut)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        imgProfile = view.findViewById(R.id.imgProfile)

        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnEditProfile.setOnClickListener {
            findNavController()
                .navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        btnSettings.setOnClickListener {
            findNavController()
                .navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        btnSignOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(
                        requireContext(),
                        "Signed out successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController()
                        .navigate(R.id.action_profileFragment_to_welcomeFragment)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        bottomNav.selectedItemId = R.id.nav_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    findNavController().navigate(R.id.dashboardFragment)

                R.id.nav_create ->
                    findNavController().navigate(R.id.myTripsFragment)

                R.id.nav_notifications ->
                    findNavController().navigate(R.id.notificationsFragment)

                R.id.nav_profile ->
                    Toast.makeText(requireContext(), "Profile", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data

            if (imageUri != null) {
                imgProfile.setImageURI(imageUri)
                Toast.makeText(
                    requireContext(),
                    "Profile photo updated!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No image selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
