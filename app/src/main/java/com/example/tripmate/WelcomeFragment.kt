package com.example.tripmate

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tripmate.data.utils.SessionManager
import androidx.navigation.NavOptions

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGetStarted = view.findViewById<Button>(R.id.btnGetStarted)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        val sessionManager = SessionManager(requireContext())

        // Auto-login if user session exists
        if (sessionManager.getUserId() != SessionManager.NO_USER) {
            view.findNavController().navigate(
                R.id.action_welcomeFragment_to_dashboardFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.welcomeFragment, true)
                    .build()
            )
            return
        }

        btnGetStarted.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_signupFragment)
        }

        btnLogin.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }
}