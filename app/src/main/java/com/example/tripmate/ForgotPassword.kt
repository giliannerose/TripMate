package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnSendResetLink = view.findViewById<Button>(R.id.btnSendResetLink)
        val tvBackLogin = view.findViewById<TextView>(R.id.tvBackLogin)

        btnSendResetLink.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(
                requireContext(),
                "Password reset link sent to $email",
                Toast.LENGTH_LONG
            ).show()

            view.findNavController()
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }

        tvBackLogin.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
    }


}