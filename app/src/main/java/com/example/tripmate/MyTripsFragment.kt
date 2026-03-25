package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.ui.trip.TripViewModel
import com.example.tripmate.ui.trip.TripAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration


class MyTripsFragment : Fragment(R.layout.fragment_my_trips) {

    private lateinit var adapter: TripAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyStateLayout: LinearLayout
    private val db = FirebaseFirestore.getInstance()

    private var tripListener: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCreateTrip = view.findViewById<Button>(R.id.btnCreateTrip)
        val btnEmptyCreateTrip = view.findViewById<Button>(R.id.btnEmptyCreateTrip)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        recyclerView = view.findViewById(R.id.tripsRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout)


        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        adapter = TripAdapter(
            onClick = { trip ->

                val action =
                    MyTripsFragmentDirections
                        .actionMyTripsFragmentToTripDetailsFragment(
                            trip.id,
                            trip.name,
                            trip.date
                        )

                view.findNavController().navigate(action)
            },

            onDelete = { trip ->
                showDeleteConfirmation(trip, userId)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadTrips(userId)

        btnCreateTrip.setOnClickListener {
            view.findNavController()
                .navigate(R.id.createTripFragment)
        }

        btnEmptyCreateTrip.setOnClickListener {
            view.findNavController().navigate(R.id.createTripFragment)
        }


        bottomNav.selectedItemId = R.id.nav_create



        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    view.findNavController().navigate(R.id.dashboardFragment)

                R.id.nav_create ->
                    Toast.makeText(requireContext(), "You're already on Trips", Toast.LENGTH_SHORT).show()

                R.id.nav_notifications ->
                    view.findNavController().navigate(R.id.notificationsFragment)

                R.id.nav_profile ->
                    view.findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }

    private fun loadTrips(userId: String) {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        emptyStateLayout.visibility = View.GONE

        tripListener?.remove()
        tripListener = db.collection("trips")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { result, error ->
                progressBar.visibility = View.GONE

                if (error != null) {
                    recyclerView.visibility = View.GONE
                    emptyStateLayout.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Failed to load trips", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val trips = mutableListOf<Trip>()

                result?.documents?.forEach { document ->
                    val trip = Trip(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        date = document.getString("date") ?: ""
                    )
                    trips.add(trip)
                }

                val sortedTrips = trips.sortedBy { it.name.lowercase() }

                adapter.submitList(sortedTrips)

                if (sortedTrips.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyStateLayout.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyStateLayout.visibility = View.GONE
                }
            }
    }

    private fun showDeleteConfirmation(trip: Trip, userId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Trip?")
            .setMessage("Delete \"${trip.name}\" permanently?")
            .setPositiveButton("Delete") { _, _ ->
                db.collection("trips")
                    .document(trip.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Trip deleted", Toast.LENGTH_SHORT).show()

                        onViewCreated(requireView(), null)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

        override fun onDestroyView() {
            super.onDestroyView()
            tripListener?.remove()
        }

}
