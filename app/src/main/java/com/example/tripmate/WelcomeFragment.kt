package com.example.tripmate

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tripmate.data.utils.SessionManager
import androidx.navigation.NavOptions
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGetStarted = view.findViewById<Button>(R.id.btnGetStarted)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
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