package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.repository.UserRepository
import androidx.lifecycle.lifecycleScope
import com.example.tripmate.data.utils.SessionManager
import kotlinx.coroutines.launch


class SignupFragment : Fragment(R.layout.fragment_sign_up) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnCreateAccount = view.findViewById<Button>(R.id.btnCreateAccount)
        val btnBackLogin = view.findViewById<Button>(R.id.btnBackLogin)

        btnCreateAccount.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            // Validation
            var isValid = true
            if (name.isEmpty()) {
                etName.error = "Name is required"; isValid = false
            }

            if (email.isEmpty()) {
                etEmail.error = "Email is required"; isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email address"
                isValid = false
            }

            if (password.isEmpty()) {
                etPassword.error = "Password is required"; isValid = false
            } else if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                isValid = false
            }



            if (isValid) {
                //Setup the Repository
                val database = AppDatabase.getDatabase(requireContext())
                val repository = UserRepository(database.userDao())
                // Launch Coroutine to save the User
                viewLifecycleOwner.lifecycleScope.launch {
                    val userId = repository.register(name, email, password)

                    if (userId != null) {

                        val sessionManager = SessionManager(requireContext())
                        sessionManager.saveUserSession(userId.toInt(), name)

                        Toast.makeText(
                            requireContext(),
                            "Welcome, $name!",
                            Toast.LENGTH_SHORT
                        ).show()

                        view.findNavController()
                            .navigate(R.id.action_signupFragment_to_profileSetupFragment)

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Email already registered!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
            btnBackLogin.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }

    }


