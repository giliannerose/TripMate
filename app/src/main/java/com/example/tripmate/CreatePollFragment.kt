package com.example.tripmate.ui.polls

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class CreatePollFragment : Fragment(R.layout.fragment_create_poll) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etPollQuestion = view.findViewById<EditText>(R.id.etPollQuestion)
        val etOption1 = view.findViewById<EditText>(R.id.etOption1)
        val etOption2 = view.findViewById<EditText>(R.id.etOption2)
        val etOption3 = view.findViewById<EditText>(R.id.etOption3)
        val etOption4 = view.findViewById<EditText>(R.id.etOption4)

        val btnCreatePoll = view.findViewById<Button>(R.id.btnCreatePoll)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        btnCreatePoll.setOnClickListener {

            val question = etPollQuestion.text.toString().trim()
            val option1 = etOption1.text.toString().trim()
            val option2 = etOption2.text.toString().trim()

            etPollQuestion.error = null
            etOption1.error = null
            etOption2.error = null

            if (question.isEmpty()) {
                etPollQuestion.error = "Poll question is required"
                etPollQuestion.requestFocus()
                return@setOnClickListener
            }

            if (option1.isEmpty()) {
                etOption1.error = "At least two options are required"
                etOption1.requestFocus()
                return@setOnClickListener
            }

            if (option2.isEmpty()) {
                etOption2.error = "At least two options are required"
                etOption2.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(
                requireContext(),
                "Poll created successfully",
                Toast.LENGTH_SHORT
            ).show()

            findNavController()
                .navigate(R.id.action_createPollFragment_to_voteFragment)
        }

        // Top tabs navigation
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            findNavController()
                .navigate(R.id.action_createPollFragment_to_tripDetailsFragment)
        }

        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {
            Toast.makeText(requireContext(), "You're already on Polls", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            findNavController()
                .navigate(R.id.action_createPollFragment_to_expenseSummaryFragment)
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {
            findNavController()
                .navigate(R.id.action_createPollFragment_to_documentsFragment)
        }

        view.findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            findNavController()
                .navigate(R.id.action_createPollFragment_to_itineraryFragment)
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
