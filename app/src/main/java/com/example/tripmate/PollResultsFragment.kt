package com.example.tripmate.ui.polls

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PollResultsFragment : Fragment(R.layout.fragment_poll_results) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        // Top Tabs
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            findNavController().navigate(R.id.tripDetailsFragment)
        }

        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {
            Toast.makeText(requireContext(), "You're already on Polls", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            findNavController().navigate(R.id.expenseSummaryFragment)
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {
            findNavController().navigate(R.id.documentsFragment)
        }

        view.findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            findNavController().navigate(R.id.itineraryFragment)
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

        // Back to polls buttons
        listOf(R.id.btnBackPolls, R.id.btnBackPolls1).forEach { id ->
            view.findViewById<Button>(id).setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}
