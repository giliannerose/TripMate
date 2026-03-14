package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tripmate.data.utils.SessionManager
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.repository.UserRepository
import kotlinx.coroutines.launch



class LoginFragment : Fragment(R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvForgotPassword = view.findViewById<TextView>(R.id.tvForgotPassword)
        val database = AppDatabase.getDatabase(requireContext())
        val repository = UserRepository(database.userDao())
        val sessionManager = SessionManager(requireContext())


        swipeRefresh.setOnRefreshListener {
            // Clear inputs
            etEmail.text.clear()
            etPassword.text.clear()

            // Clear errors
            etEmail.error = null
            etPassword.error = null


            swipeRefresh.isRefreshing = false

            Toast.makeText(requireContext(), "Inputs cleared", Toast.LENGTH_SHORT).show()

        }


        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            etEmail.error = null
            etPassword.error = null

            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Invalid email format"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // Launch Coroutine to check database
            viewLifecycleOwner.lifecycleScope.launch {
                val user = repository.login(email, password)

                if (user != null) {
                    // SUCCESS: Save name and move to Dashboard
                    sessionManager.saveUserSession(user.id, user.name)
                    Toast.makeText(
                        requireContext(),
                        "Welcome back, ${user.name}!",
                        Toast.LENGTH_SHORT
                    ).show()
                    view.findNavController()
                        .navigate(R.id.action_loginFragment_to_loginSuccessFragment)
                } else {
                    // FAILURE
                    Toast.makeText(
                        requireContext(),
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
                btnRegister.setOnClickListener {
                    view.findNavController()
                        .navigate(R.id.action_loginFragment_to_signupFragment)
                }

                tvForgotPassword.setOnClickListener {
                    view.findNavController()
                        .navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
                }
            }

        }


