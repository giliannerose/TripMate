package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController


class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGetStarted = view.findViewById<Button>(R.id.btnGetStarted)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

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