package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tripmate.data.utils.SessionManager
import com.example.tripmate.ui.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvForgotPassword = view.findViewById<TextView>(R.id.tvForgotPassword)
        val auth = FirebaseAuth.getInstance()


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

            btnLogin.isEnabled = false

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    btnLogin.isEnabled = true

                    Toast.makeText(
                        requireContext(),
                        "Welcome back!",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(
                        R.id.action_loginFragment_to_loginSuccessFragment
                    )
                }
                .addOnFailureListener {

                    btnLogin.isEnabled = true

                    Toast.makeText(
                        requireContext(),
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }



        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }
}
