package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.ui.trip.TripViewModel
import com.example.tripmate.ui.trip.TripAdapter
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.data.utils.SessionManager



class MyTripsFragment : Fragment(R.layout.fragment_my_trips) {

    private lateinit var viewModel: TripViewModel
    private lateinit var adapter: TripAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCreateTrip = view.findViewById<Button>(R.id.btnCreateTrip)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        val btnInvite = view.findViewById<Button>(R.id.btnInvite)
        val btnExpense = view.findViewById<Button>(R.id.btnExpense)

        val session = SessionManager(requireContext())
        val userId = session.getUserId()


        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.tripsRecyclerView)

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
                showDeleteConfirmation(trip)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getTripsByUser(userId).observe(viewLifecycleOwner) { trips ->
            adapter.submitList(trips)
        }

        bottomNav.selectedItemId = R.id.nav_create

        btnCreateTrip.setOnClickListener {
            view.findNavController()
                .navigate(R.id.createTripFragment)
        }



        btnInvite.setOnClickListener {
            view.findNavController()
                .navigate(R.id.inviteMembersFragment)
        }

        btnExpense.setOnClickListener {
            view.findNavController()
                .navigate(R.id.expenseSummaryFragment)
        }





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

    private fun showDeleteConfirmation(trip: TripEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Trip?")
            .setMessage("Delete \"${trip.name}\" permanently?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.delete(trip)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



}
