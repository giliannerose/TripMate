package com.example.tripmate

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.tripmate.data.utils.SessionManager
import com.example.tripmate.ui.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment(R.layout.fragment_sign_up) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val etName = view.findViewById<EditText>(R.id.etName)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnCreateAccount = view.findViewById<Button>(R.id.btnCreateAccount)
        val btnBackLogin = view.findViewById<Button>(R.id.btnBackLogin)

        btnCreateAccount.setOnClickListener {

            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                etName.error = "Name is required"
                isValid = false
            }

            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email address"
                isValid = false
            }

            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                isValid = false
            } else if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                isValid = false
            }

            if (isValid) {

                val db = FirebaseFirestore.getInstance()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {

                        val userId = auth.currentUser!!.uid

                        val user = hashMapOf(
                            "name" to name,
                            "bio" to "",
                            "gender" to "",
                            "region" to "",
                            "age" to 0,
                            "profileImageUri" to "",
                            "createdAt" to System.currentTimeMillis()
                        )

                        db.collection("users")
                            .document(userId)
                            .set(user)
                            .addOnSuccessListener {

                                Toast.makeText(
                                    requireContext(),
                                    "Welcome, $name",
                                    Toast.LENGTH_SHORT
                                ).show()

                                view.findNavController()
                                    .navigate(R.id.action_signupFragment_to_loginSuccessFragment)
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Signup failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }



        btnBackLogin.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
}