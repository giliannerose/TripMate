package com.example.tripmate.ui.notifications

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        val btnDecline = view.findViewById<Button>(R.id.btnDecline)
        val btnViewPoll = view.findViewById<Button>(R.id.btnViewPoll)
        val btnViewExpenses = view.findViewById<Button>(R.id.btnViewExpenses)
        val btnAcknowledge = view.findViewById<Button>(R.id.btnAcknowledge)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_notifications

        // Accept invitation
        btnAccept.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Accept Invitation")
                .setMessage("Are you sure you want to accept this invitation?")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(requireContext(), "Invitation accepted!", Toast.LENGTH_SHORT).show()

                    disableInvitationActions(
                        btnAccept,
                        btnDecline,
                        btnViewPoll,
                        btnViewExpenses
                    )
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }

        // Decline invitation
        btnDecline.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Decline Invitation")
                .setMessage("Are you sure you want to decline this invitation?")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(requireContext(), "Invitation declined.", Toast.LENGTH_SHORT).show()

                    btnAccept.isEnabled = false
                    btnDecline.isEnabled = false

                    btnAccept.setBackgroundColor(
                        requireContext().resources.getColor(android.R.color.darker_gray)
                    )
                    btnDecline.setBackgroundColor(
                        requireContext().resources.getColor(android.R.color.darker_gray)
                    )

                    btnAccept.setTextColor(
                        requireContext().resources.getColor(android.R.color.white)
                    )
                    btnDecline.setTextColor(
                        requireContext().resources.getColor(android.R.color.white)
                    )
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }

        btnViewPoll.setOnClickListener {
            findNavController()
                .navigate(R.id.action_notificationsFragment_to_voteFragment)
        }

        btnViewExpenses.setOnClickListener {
            findNavController()
                .navigate(R.id.action_notificationsFragment_to_expenseSummaryFragment)
        }

        btnAcknowledge.setOnClickListener {
            Toast.makeText(requireContext(), "Acknowledged.", Toast.LENGTH_SHORT).show()
            btnAcknowledge.isEnabled = false
            btnAcknowledge.setBackgroundColor(
                requireContext().resources.getColor(android.R.color.darker_gray)
            )
            btnAcknowledge.setTextColor(
                requireContext().resources.getColor(android.R.color.white)
            )
        }

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.dashboardFragment)
                }
                R.id.nav_create -> {
                    findNavController().navigate(R.id.myTripsFragment)
                }
                R.id.nav_notifications -> {
                    Toast.makeText(
                        requireContext(),
                        "You're already on Notifications",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.nav_profile -> {
                    findNavController().navigate(R.id.profileFragment)
                }
            }
            true
        }
    }

    private fun disableInvitationActions(
        btnAccept: Button,
        btnDecline: Button,
        btnViewPoll: Button,
        btnViewExpenses: Button
    ) {
        val buttons = listOf(btnAccept, btnDecline, btnViewPoll, btnViewExpenses)

        buttons.forEach { button ->
            button.isEnabled = false
            button.setBackgroundColor(
                requireContext().resources.getColor(android.R.color.darker_gray)
            )
            button.setTextColor(
                requireContext().resources.getColor(android.R.color.white)
            )
        }
    }
}
