package com.example.tripmate

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tripmate.data.utils.SessionManager
import com.example.tripmate.ui.user.UserViewModel
import com.example.tripmate.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Binding
        _binding = FragmentLoginBinding.bind(view)

        val sessionManager = SessionManager(requireContext())

        // Setup Swipe Refresh
        binding.swipeRefresh.setOnRefreshListener {
            binding.etEmail.text.clear()
            binding.etPassword.text.clear()
            binding.etEmail.error = null
            binding.etPassword.error = null
            binding.swipeRefresh.isRefreshing = false
        }

        // Login Button Logic
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validate(email, password)) {
                // Delegate the heavy lifting to the ViewModel
                userViewModel.login(email, password)
            }
        }

        // Observe Firebase Login Result
        userViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            val (success, message) = result

            if (success) {
                val email = binding.etEmail.text.toString().trim()
                // 'message' contains the Firebase UID on success
                // We save email to session to fetch user details later
                sessionManager.saveUserSession(message ?: "unknown", "User", email)

                Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_loginSuccessFragment)
            } else {
                // 'message' contains the error reason on failure
                Toast.makeText(requireContext(), "Login Failed: $message", Toast.LENGTH_LONG).show()
            }
        }

        // Navigation Listeners
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    // Input Validation Helper
    private fun validate(email: String, pass: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email format"
            isValid = false
        }

        if (pass.isEmpty()) {
            binding.etPassword.error = "Password is required"
            isValid = false
        } else if (pass.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}