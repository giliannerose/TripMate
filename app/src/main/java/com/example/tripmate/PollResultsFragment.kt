package com.example.tripmate.ui.polls

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.ui.poll.PollViewModel
import com.example.tripmate.ui.poll.PollResultsAdapter
import androidx.navigation.fragment.navArgs

class PollResultsFragment : Fragment(R.layout.fragment_poll_results) {

    private lateinit var viewModel: PollViewModel
    private lateinit var adapter: PollResultsAdapter

    private val args: PollResultsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PollViewModel::class.java]

        val tripId = args.tripId
        val tripTitle = args.tripTitle
        val tripDate = args.tripDate

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerResults)

        adapter = PollResultsAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.getPollsByTrip(tripId).observe(viewLifecycleOwner) { polls ->
            adapter.submitList(polls)
        }

        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        // Top Tabs
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            val action =
                PollResultsFragmentDirections
                    .actionPollResultsFragmentToTripDetailsFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {
            Toast.makeText(requireContext(), "You're already on Polls", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            val action =
           PollResultsFragmentDirections
                    .actionPollResultsFragmentToExpenseSummaryFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {
            val action =
                PollResultsFragmentDirections
                    .actionPollResultsFragmentToDocumentsFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            val action =
                PollResultsFragmentDirections
                    .actionPollResultsFragmentToItineraryFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        // Bottom Navigation
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
