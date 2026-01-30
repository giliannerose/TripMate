package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.findNavController

class LoginSuccessFragment : Fragment(R.layout.fragment_login_success) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGoToDashboard = view.findViewById<Button>(R.id.btnGoToDashboard)

        btnGoToDashboard.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_loginSuccessFragment_to_dashboardFragment)
        }
    }

}