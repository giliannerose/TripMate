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



class MyTripsFragment : Fragment(R.layout.fragment_my_trips) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCreateTrip = view.findViewById<Button>(R.id.btnCreateTrip)
        val cardSiargao = view.findViewById<CardView>(R.id.cardSiargao)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        val btnInvite = view.findViewById<Button>(R.id.btnInvite)
        val btnExpense = view.findViewById<Button>(R.id.btnExpense)

        bottomNav.selectedItemId = R.id.nav_create

        btnCreateTrip.setOnClickListener {
            view.findNavController()
                .navigate(R.id.createTripFragment)
        }

        cardSiargao.setOnClickListener {
            view.findNavController()
                .navigate(R.id.tripDetailsFragment)
        }

        btnInvite.setOnClickListener {
            view.findNavController()
                .navigate(R.id.inviteMembersFragment)
        }

        btnExpense.setOnClickListener {
            view.findNavController()
                .navigate(R.id.expenseSummaryFragment)
        }

        val btnDeleteNewTrip = view.findViewById<Button>(R.id.btnDeleteNewTrip)
        val btnDeleteSiargao = view.findViewById<Button>(R.id.btnDeleteSiargao)
        val btnDeleteMadrid = view.findViewById<Button>(R.id.btnDeleteMadrid)

        val cardNewTrip = view.findViewById<CardView>(R.id.cardNewTrip)
        val cardMadrid = view.findViewById<CardView>(R.id.cardMadrid)

        btnDeleteNewTrip.setOnClickListener {
            showDeleteConfirmation("New Trip", cardNewTrip)
        }

        btnDeleteSiargao.setOnClickListener {
            showDeleteConfirmation("Siargao Weekend", cardSiargao)
        }

        btnDeleteMadrid.setOnClickListener {
            showDeleteConfirmation("Madrid x Barcelona", cardMadrid)
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

    private fun showDeleteConfirmation(tripName: String, tripCard: CardView) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Trip?")
            .setMessage(
                "This will permanently delete the trip \"$tripName\" and its itineraries. This action cannot be undone."
            )
            .setPositiveButton("Delete") { dialog, _ ->
                tripCard.visibility = View.GONE
                Toast.makeText(requireContext(), "$tripName deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



}
