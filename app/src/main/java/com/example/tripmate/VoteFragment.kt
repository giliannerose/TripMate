package com.example.tripmate.ui.polls

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class VoteFragment : Fragment(R.layout.fragment_vote) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rgPoll1 = view.findViewById<RadioGroup>(R.id.rgPoll1)
        val rgPoll2 = view.findViewById<RadioGroup>(R.id.rgPoll2)
        val btnSubmitVote1 = view.findViewById<Button>(R.id.btnSubmitVote1)
        val btnSubmitVote2 = view.findViewById<Button>(R.id.btnSubmitVote2)
        val btnViewResults = view.findViewById<Button>(R.id.btnViewResults)

        val tabParticipants = view.findViewById<Button>(R.id.tabParticipants)
        val tabPolls = view.findViewById<Button>(R.id.tabPolls)
        val tabExpenses = view.findViewById<Button>(R.id.tabExpenses)
        val tabDocs = view.findViewById<Button>(R.id.tabDocs)
        val tabItinerary = view.findViewById<Button>(R.id.tabItinerary)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        // Poll 1 submit
        btnSubmitVote1.setOnClickListener {
            val selectedId = rgPoll1.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(
                    requireContext(),
                    "Please select an option for Poll 1",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val option =
                    view.findViewById<RadioButton>(selectedId).text

                Toast.makeText(
                    requireContext(),
                    "Poll 1 vote submitted: $option",
                    Toast.LENGTH_SHORT
                ).show()

                btnSubmitVote1.isEnabled = false
                btnSubmitVote1.alpha = 0.5f
                rgPoll1.isEnabled = false

                for (i in 0 until rgPoll1.childCount) {
                    rgPoll1.getChildAt(i).isEnabled = false
                }
            }
        }

        // Poll 2 submit
        btnSubmitVote2.setOnClickListener {
            val selectedId = rgPoll2.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(
                    requireContext(),
                    "Please select an option for Poll 2",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val option =
                    view.findViewById<RadioButton>(selectedId).text

                Toast.makeText(
                    requireContext(),
                    "Poll 2 vote submitted: $option",
                    Toast.LENGTH_SHORT
                ).show()

                btnSubmitVote2.isEnabled = false
                btnSubmitVote2.alpha = 0.5f
                rgPoll2.isEnabled = false

                for (i in 0 until rgPoll2.childCount) {
                    rgPoll2.getChildAt(i).isEnabled = false
                }
            }
        }

        // Tabs navigation
        tabParticipants.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_tripDetailsFragment)
        }

        tabPolls.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "You're already on Polls",
                Toast.LENGTH_SHORT
            ).show()
        }

        tabExpenses.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_expenseSummaryFragment)
        }

        tabDocs.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_documentsFragment)
        }

        tabItinerary.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_itineraryFragment)
        }

        btnViewResults.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_pollResultsFragment)
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
