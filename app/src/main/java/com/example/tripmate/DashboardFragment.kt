package com.example.tripmate

import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tripmate.data.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.ui.trip.DashboardTripAdapter
import com.example.tripmate.ui.trip.TripViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tripmate.Trip

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var tripViewModel: TripViewModel
    private lateinit var adapter: DashboardTripAdapter

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripViewModel = ViewModelProvider(this)[TripViewModel::class.java]

        val recyclerTrips = view.findViewById<RecyclerView>(R.id.recyclerTrips)

        adapter = DashboardTripAdapter { trip ->

            val action =
                DashboardFragmentDirections
                    .actionDashboardFragmentToTripDetailsFragment(
                        trip.id,
                        trip.name,
                        trip.date
                    )

            view.findNavController().navigate(action)
        }


        recyclerTrips.layoutManager = LinearLayoutManager(requireContext())
        recyclerTrips.adapter = adapter


        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        val actionCreate = view.findViewById<LinearLayout>(R.id.actionCreate)
        val actionInvite = view.findViewById<LinearLayout>(R.id.actionInvite)
        val actionExpenses = view.findViewById<LinearLayout>(R.id.actionExpenses)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)

        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        tvGreeting.text = "Good day, ${currentUser.email ?: "Traveler"} 👋"

        // Swipe refresh
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
            }



        actionCreate.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_dashboardFragment_to_createTripFragment)
        }

        actionInvite.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_dashboardFragment_to_inviteMembersFragment)
        }

        actionExpenses.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_dashboardFragment_to_addExpenseFragment)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }

                R.id.nav_create -> {
                    view.findNavController()
                        .navigate(R.id.action_dashboardFragment_to_myTripsFragment)
                    true
                }

                R.id.nav_notifications -> {
                    view.findNavController()
                        .navigate(R.id.action_dashboardFragment_to_notificationsFragment)
                    true
                }

                R.id.nav_profile -> {
                    view.findNavController()
                        .navigate(R.id.action_dashboardFragment_to_profileFragment)
                    true
                }

                else -> false
            }
        }

        loadTrips(userId)
    }

    private fun loadTrips(userId: String) {
        db.collection("trips")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result: com.google.firebase.firestore.QuerySnapshot ->

                val tripList = result.map { document ->
                    Trip(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        date = document.getString("date") ?: ""
                    )
                }

                adapter.submitList(tripList)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load trips", Toast.LENGTH_SHORT).show()
            }
    }
}
