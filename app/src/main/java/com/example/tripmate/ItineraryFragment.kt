package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AlertDialog



class ItineraryFragment : Fragment(R.layout.fragment_itinerary) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val btnAddActivity = view.findViewById<Button>(R.id.btnAddActivity)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        swipeRefresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Itinerary refreshed", Toast.LENGTH_SHORT).show()
            swipeRefresh.isRefreshing = false
        }

        btnAddActivity.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_itineraryFragment_to_addActivityFragment)
        }

        // Day 1
        val day1Layout = view.findViewById<View>(R.id.include_day1_activity)
        val day1Title = day1Layout.findViewById<TextView>(R.id.tvActivityTitle)
        val day1Time = day1Layout.findViewById<TextView>(R.id.tvActivityTime)
        val day1Edit = day1Layout.findViewById<Button>(R.id.btnEditActivity)
        val day1Delete = day1Layout.findViewById<Button>(R.id.btnDeleteActivity)

        // Day 2
        val day2Layout = view.findViewById<View>(R.id.include_day2_activity)
        val day2Title = day2Layout.findViewById<TextView>(R.id.tvActivityTitle)
        val day2Time = day2Layout.findViewById<TextView>(R.id.tvActivityTime)
        val day2Edit = day2Layout.findViewById<Button>(R.id.btnEditActivity)
        val day2Delete = day2Layout.findViewById<Button>(R.id.btnDeleteActivity)

        day1Edit.setOnClickListener { showEditDialog(day1Title, day1Time) }
        day2Edit.setOnClickListener { showEditDialog(day2Title, day2Time) }

        day1Delete.setOnClickListener {
            showDeleteDialog(day1Layout, "Day 1 activity deleted!")
        }

        day2Delete.setOnClickListener {
            showDeleteDialog(day2Layout, "Day 2 activity deleted!")
        }

        // Top tabs
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_itineraryFragment_to_tripDetailsFragment)
        }

        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_itineraryFragment_to_createPollFragment)
        }

        view.findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_itineraryFragment_to_expenseSummaryFragment)
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_itineraryFragment_to_documentsFragment)
        }

        view.findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            Toast.makeText(requireContext(), "You're already on Itinerary", Toast.LENGTH_SHORT).show()
        }

        // Remove highlight
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

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

    private fun showEditDialog(titleView: TextView, timeView: TextView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_activity, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etTime = dialogView.findViewById<EditText>(R.id.etTime)

        etTitle.setText(titleView.text.toString())
        etTime.setText(timeView.text.toString())

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Activity")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                var isValid = true

                if (etTitle.text.isBlank()) {
                    etTitle.error = "Title is required"
                    isValid = false
                }

                if (etTime.text.isBlank()) {
                    etTime.error = "Time is required"
                    isValid = false
                }

                if (isValid) {
                    titleView.text = etTitle.text.toString()
                    timeView.text = etTime.text.toString()
                    Toast.makeText(requireContext(), "Activity updated!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(layoutToRemove: View, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Activity")
            .setMessage("Are you sure you want to delete this activity?")
            .setPositiveButton("Delete") { _, _ ->
                layoutToRemove.visibility = View.GONE
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



}
