package com.example.tripmate.ui.itinerary


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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripmate.data.model.ActivityEntity

import com.example.tripmate.ui.itinerary.ActivityAdapter
import com.example.tripmate.ui.itinerary.ActivityViewModel
import com.example.tripmate.R
import androidx.navigation.fragment.navArgs


class ItineraryFragment : Fragment(R.layout.fragment_itinerary) {

    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var adapter: ActivityAdapter
    private val args: ItineraryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                // Initialize ViewModel
                activityViewModel = ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory
                        .getInstance(requireActivity().application)
                )[ActivityViewModel::class.java]

        // Setup RecyclerView
                val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerActivities)

            adapter = ActivityAdapter(
                onEditClick = { activity ->
                    showEditDialog(activity)
                },
                onDeleteClick = { activity ->
                    activityViewModel.delete(activity.id)
                }
            )
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val tripId = args.tripId
        val tripTitle = args.tripTitle
        val tripDate = args.tripDate

        val tripTitleView = view.findViewById<TextView>(R.id.tvTripTitle)
        val tripDateView = view.findViewById<TextView>(R.id.tvTripDate)

        tripTitleView.text = args.tripTitle
        tripDateView.text = args.tripDate

        activityViewModel.getActivitiesForTrip(tripId)
            .observe(viewLifecycleOwner) { activities ->
                adapter.setActivities(activities)
            }

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val btnAddActivity = view.findViewById<Button>(R.id.btnAddActivity)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        swipeRefresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Itinerary refreshed", Toast.LENGTH_SHORT).show()
            swipeRefresh.isRefreshing = false
        }

        btnAddActivity.setOnClickListener {
            val action =
                ItineraryFragmentDirections
                    .actionItineraryFragmentToAddActivityFragment(args.tripId)

            findNavController().navigate(action)
        }



        // Top tabs
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            val action =
                ItineraryFragmentDirections
                    .actionItineraryFragmentToTripDetailsFragment(
                        tripId,
                        args.tripTitle,
                        args.tripDate
                    )

            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {

            val action =
                ItineraryFragmentDirections
                    .actionItineraryFragmentToCreatePollFragment(
                        args.tripId,
                        args.tripTitle,
                        args.tripDate
                    )

            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabExpenses).setOnClickListener {

            val action =
                ItineraryFragmentDirections
                    .actionItineraryFragmentToExpenseSummaryFragment(
                        args.tripId,
                        args.tripTitle,
                        args.tripDate
                    )

            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {

            val action =
                ItineraryFragmentDirections
                    .actionItineraryFragmentToDocumentsFragment(
                        args.tripId,
                        args.tripTitle,
                        args.tripDate
                    )

            findNavController().navigate(action)
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

    private fun showEditDialog(activity: ActivityEntity) {

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_activity, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etTime = dialogView.findViewById<EditText>(R.id.etTime)

        etTitle.setText(activity.title)
        etTime.setText(activity.time)

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

                    val updatedActivity = activity.copy(
                        title = etTitle.text.toString(),
                        time = etTime.text.toString()
                    )

                    activityViewModel.update(updatedActivity)

                    Toast.makeText(
                        requireContext(),
                        "Activity updated!",
                        Toast.LENGTH_SHORT
                    ).show()

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
