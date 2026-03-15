package com.example.tripmate.ui.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import androidx.lifecycle.ViewModelProvider
import com.example.tripmate.ui.user.UserViewModel
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.utils.PasswordHasher


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var viewModel: UserViewModel
    private var currentUser: UserEntity? = null

    // temporary
    private val userId = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val tvBack = view.findViewById<TextView>(R.id.tvBack)
        val switchNotifications = view.findViewById<Switch>(R.id.switchNotifications)
        val tvPrivacyStatus = view.findViewById<TextView>(R.id.tvPrivacyStatus)
        val etCurrentPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnSaveSettings = view.findViewById<Button>(R.id.btnSaveSettings)

        viewModel.getUserById(userId).observe(viewLifecycleOwner) { user ->

            if (user != null) {
                currentUser = user

                switchNotifications.isChecked = user.notificationsEnabled
                tvPrivacyStatus.text = user.privacyStatus
            }
        }

        // Back
        tvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        fun updateSwitchColor(isChecked: Boolean) {
            if (isChecked) {
                switchNotifications.thumbTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.blue))
                switchNotifications.trackTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.blue_light))
            } else {
                switchNotifications.thumbTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.gray))
                switchNotifications.trackTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.gray_light))
            }
        }



        // Notifications toggle
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->

            updateSwitchColor(isChecked)

            val message =
                if (isChecked) "Notifications enabled"
                else "Notifications disabled"

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // Privacy toggle
        tvPrivacyStatus.setOnClickListener {
            tvPrivacyStatus.text =
                if (tvPrivacyStatus.text == "Public") "Private" else "Public"
        }

        // Save settings
        btnSaveSettings.setOnClickListener {

            val currentPass = etCurrentPassword.text.toString().trim()
            val newPass = etNewPassword.text.toString().trim()
            val confirmPass = etConfirmPassword.text.toString().trim()

            var isValid = true

            val anyPasswordEntered =
                currentPass.isNotEmpty() ||
                        newPass.isNotEmpty() ||
                        confirmPass.isNotEmpty()

            if (anyPasswordEntered) {

                if (currentPass.isEmpty()) {
                    etCurrentPassword.error = "Current password is required"
                    isValid = false
                }

                if (newPass.isEmpty()) {
                    etNewPassword.error = "New password is required"
                    isValid = false
                } else if (newPass.length < 6) {
                    etNewPassword.error = "Password must be at least 6 characters"
                    isValid = false
                }

                if (confirmPass.isEmpty()) {
                    etConfirmPassword.error = "Please confirm your new password"
                    isValid = false
                } else if (newPass != confirmPass) {
                    etConfirmPassword.error = "Passwords do not match"
                    isValid = false
                }
            }


            if (!isValid) return@setOnClickListener

            // only show dialog if valid
            AlertDialog.Builder(requireContext())
                .setTitle("Save Settings")
                .setMessage("Are you sure you want to save these changes?")
                .setPositiveButton("Yes") { _, _ ->
                    val user = currentUser ?: return@setPositiveButton

                    val updatedUser = user.copy(
                        notificationsEnabled = switchNotifications.isChecked,
                        privacyStatus = tvPrivacyStatus.text.toString(),
                        passwordHash =
                            if (newPass.isNotEmpty())
                                PasswordHasher.hash(newPass)
                            else
                                user.passwordHash
                    )

                    viewModel.update(updatedUser)

                    Toast.makeText(
                        requireContext(),
                        "Settings saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

    }
}
