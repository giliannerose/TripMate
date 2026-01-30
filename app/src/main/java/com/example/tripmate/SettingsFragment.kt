package com.example.tripmate.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvBack = view.findViewById<TextView>(R.id.tvBack)
        val switchNotifications = view.findViewById<Switch>(R.id.switchNotifications)
        val tvPrivacyStatus = view.findViewById<TextView>(R.id.tvPrivacyStatus)
        val etCurrentPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnSaveSettings = view.findViewById<Button>(R.id.btnSaveSettings)

        // Back
        tvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Notifications toggle
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
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

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Save Settings")
                .setMessage("Are you sure you want to save these changes?")
                .setPositiveButton("Yes", null)
                .setNegativeButton("Cancel") { d, _ ->
                    d.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Save canceled.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                    val currentPass = etCurrentPassword.text.toString().trim()
                    val newPass = etNewPassword.text.toString().trim()
                    val confirmPass = etConfirmPassword.text.toString().trim()

                    var isValid = true

                    if (newPass.isNotEmpty() || confirmPass.isNotEmpty()) {

                        if (currentPass.isEmpty()) {
                            etCurrentPassword.error = "Current password is required"
                            isValid = false
                        }

                        if (newPass.length < 6) {
                            etNewPassword.error = "Password must be at least 6 characters"
                            isValid = false
                        }

                        if (newPass != confirmPass) {
                            etConfirmPassword.error = "Passwords do not match"
                            isValid = false
                        }
                    }

                    if (isValid) {
                        Toast.makeText(
                            requireContext(),
                            "Settings saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()
                        findNavController().popBackStack()
                    }
                }
            }

            dialog.show()
        }
    }
}
