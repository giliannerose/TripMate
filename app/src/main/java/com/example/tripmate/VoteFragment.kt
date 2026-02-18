package com.example.tripmate.ui.polls

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.data.model.PollEntity
import com.example.tripmate.ui.poll.PollViewModel
import com.example.tripmate.ui.poll.PollAdapter

class VoteFragment : Fragment(R.layout.fragment_vote) {

    private lateinit var viewModel: PollViewModel
    private lateinit var adapter: PollAdapter
    private val tripId: Long = 1   // TEMP for Sprint 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PollViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerPolls)

        adapter = PollAdapter { poll: PollEntity, selectedOption: String ->
            Toast.makeText(
                requireContext(),
                "Vote submitted: $selectedOption",
                Toast.LENGTH_SHORT
            ).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.getPollsByTrip(tripId).observe(viewLifecycleOwner) { polls ->
            adapter.submitList(polls)
        }

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
