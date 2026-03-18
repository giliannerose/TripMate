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
import com.example.tripmate.data.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var viewModel: UserViewModel
    private var currentUser: UserEntity? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val tvBack = view.findViewById<TextView>(R.id.tvBack)
        val switchNotifications = view.findViewById<Switch>(R.id.switchNotifications)
        val tvPrivacyStatus = view.findViewById<TextView>(R.id.tvPrivacyStatus)
        val etCurrentPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnSaveSettings = view.findViewById<Button>(R.id.btnSaveSettings)

        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            Toast.makeText(requireContext(), "User session not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val userId = firebaseUser.uid

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val user = UserEntity(
                        id = userId,
                        name = document.getString("name") ?: "",
                        bio = document.getString("bio") ?: "",
                        gender = document.getString("gender") ?: "",
                        region = document.getString("region") ?: "",
                        age = document.getLong("age")?.toInt() ?: 0,
                        profileImageUri = document.getString("profileImageUri"),
                        createdAt = document.getLong("createdAt") ?: 0L,
                        notificationsEnabled = document.getBoolean("notificationsEnabled") ?: true,
                        privacyStatus = document.getString("privacyStatus") ?: "Public",
                        passwordHash = document.getString("passwordHash") ?: ""
                    )

                    currentUser = user

                    switchNotifications.isChecked = user.notificationsEnabled
                    tvPrivacyStatus.text = user.privacyStatus
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load settings", Toast.LENGTH_SHORT).show()
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
                if (tvPrivacyStatus.text.toString() == "Public") "Private" else "Public"
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

                    // Validate current password if user is trying to change password
                    if (newPass.isNotEmpty()) {

                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        val email = firebaseUser?.email

                        if (email == null) {
                            Toast.makeText(requireContext(), "User email not found", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }

                        val credential = com.google.firebase.auth.EmailAuthProvider
                            .getCredential(email, currentPass)

                        firebaseUser.reauthenticate(credential)
                            .addOnSuccessListener {

                                firebaseUser.updatePassword(newPass)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "Failed to update password", Toast.LENGTH_SHORT).show()
                                    }

                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show()
                            }

                        return@setPositiveButton
                    }

                    val userId = auth.currentUser?.uid ?: return@setPositiveButton

                    val updates = hashMapOf<String, Any>(
                        "notificationsEnabled" to switchNotifications.isChecked,
                        "privacyStatus" to tvPrivacyStatus.text.toString(),
                    )

                    db.collection("users")
                        .document(userId)
                        .set(updates, SetOptions.merge())
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Settings saved successfully!", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to save settings", Toast.LENGTH_SHORT).show()
                        }

                    findNavController().popBackStack()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

    }
}
