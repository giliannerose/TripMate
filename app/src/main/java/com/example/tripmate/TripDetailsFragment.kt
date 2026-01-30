package com.example.tripmate.ui.tripdetails

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TripDetailsFragment : Fragment(R.layout.fragment_trip_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabParticipants = view.findViewById<Button>(R.id.tabParticipants)
        val tabPolls = view.findViewById<Button>(R.id.tabPolls)
        val tabExpenses = view.findViewById<Button>(R.id.tabExpenses)
        val tabDocs = view.findViewById<Button>(R.id.tabDocs)
        val tabItinerary = view.findViewById<Button>(R.id.tabItinerary)
        val btnAddParticipant = view.findViewById<Button>(R.id.btnAddParticipant)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)
        val participantsContainer = view.findViewById<LinearLayout>(R.id.participantsContainer)

        // Remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        // Edit / Delete actions
        for (i in 0 until participantsContainer.childCount) {
            val card = participantsContainer.getChildAt(i)
            val editIcon = card.findViewById<ImageView>(R.id.ic_edit)
            val deleteIcon = card.findViewById<ImageView>(R.id.ic_delete)
            val nameText = card.findViewById<TextView>(R.id.tvName)

            editIcon?.setOnClickListener {
                val input = EditText(requireContext())
                input.setText(nameText?.text)

                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Edit Participant")
                    .setMessage("Update name for this participant:")
                    .setView(input)
                    .setPositiveButton("Save", null)
                    .setNegativeButton("Cancel", null)
                    .create()

                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val newName = input.text.toString().trim()

                        if (newName.isEmpty()) {
                            input.error = "Name cannot be empty"
                        } else {
                            nameText?.text = newName
                            Toast.makeText(
                                requireContext(),
                                "Participant updated!",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                    }
                }

                dialog.show()
            }

            deleteIcon?.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Remove Participant")
                    .setMessage("Are you sure you want to delete ${nameText?.text}?")
                    .setPositiveButton("Yes") { _, _ ->
                        participantsContainer.removeView(card)
                        Toast.makeText(
                            requireContext(),
                            "Participant deleted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }

        // Top tabs
        tabParticipants.setOnClickListener {
            Toast.makeText(requireContext(), "You're in Participants", Toast.LENGTH_SHORT).show()
        }

        tabPolls.setOnClickListener {
            findNavController()
                .navigate(R.id.action_tripDetailsFragment_to_createPollFragment)
        }

        tabExpenses.setOnClickListener {
            findNavController()
                .navigate(R.id.action_tripDetailsFragment_to_expenseSummaryFragment)
        }

        tabDocs.setOnClickListener {
            findNavController()
                .navigate(R.id.action_tripDetailsFragment_to_documentsFragment)
        }

        tabItinerary.setOnClickListener {
            findNavController()
                .navigate(R.id.action_tripDetailsFragment_to_itineraryFragment)
        }

        btnAddParticipant.setOnClickListener {
            findNavController()
                .navigate(R.id.action_tripDetailsFragment_to_inviteMembersFragment)
        }

        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    findNavController().navigate(R.id.dashboardFragment)

                R.id.nav_create ->
                    findNavController().navigate(R.id.myTripsFragment)

                R.id.nav_notifications ->
                    findNavController().navigate(R.id.notificationsFragment)

                R.id.nav_profile ->
                    findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }
}
