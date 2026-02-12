package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.findNavController
import com.example.tripmate.data.utils.SessionManager


class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGetStarted = view.findViewById<Button>(R.id.btnGetStarted)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val sessionManager = SessionManager(requireContext())

        // Auto-Login Check: If name is not the default user, skip to dashboard
        if (sessionManager.getUserName() != "User") {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_dashboardFragment)
            return
        }

        btnGetStarted.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_signupFragment)
        }

        btnLogin.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_WelcomeFragment_to_loginFragment)
        }
    }



}