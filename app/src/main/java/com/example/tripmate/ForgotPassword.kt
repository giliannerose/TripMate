package com.example.tripmate

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.tripmate.databinding.FragmentForgotPasswordBinding
import com.example.tripmate.ui.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding = FragmentForgotPasswordBinding.bind(view)

        val auth = FirebaseAuth.getInstance()

        binding.btnSendResetLink.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {

                    Toast.makeText(
                        requireContext(),
                        "Reset email sent!",
                        Toast.LENGTH_LONG
                    ).show()

                    requireView().findNavController()
                        .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                }
                .addOnFailureListener {

                    Toast.makeText(
                        requireContext(),
                        "Failed to send reset email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }

        binding.tvBackLogin.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}