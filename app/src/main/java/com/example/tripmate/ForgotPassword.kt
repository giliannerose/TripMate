package com.example.tripmate

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.repository.UserRepository
import com.example.tripmate.databinding.FragmentForgotPasswordBinding
import com.example.tripmate.ui.user.UserViewModel

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentForgotPasswordBinding.bind(view)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

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

            userViewModel.checkEmailExists(email) { exists ->

                if (exists) {

                    Toast.makeText(
                        requireContext(),
                        "Password reset link sent to $email",
                        Toast.LENGTH_LONG
                    ).show()

                    view.findNavController()
                        .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)

                } else {

                    Toast.makeText(
                        requireContext(),
                        "Email not found",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

        binding.tvBackLogin.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}