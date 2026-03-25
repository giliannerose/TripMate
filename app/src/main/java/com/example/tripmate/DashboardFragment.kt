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
import com.google.android.material.card.MaterialCardView
import com.example.tripmate.ui.dashboard.DashboardActivityAdapter
import com.example.tripmate.ui.dashboard.DashboardActivity


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var tripViewModel: TripViewModel
    private lateinit var adapter: DashboardTripAdapter

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tvGreeting: TextView
    private lateinit var tvEmptyTrips: TextView
    private lateinit var recyclerTrips: RecyclerView
    private lateinit var activityAdapter: DashboardActivityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripViewModel = ViewModelProvider(this)[TripViewModel::class.java]

        recyclerTrips = view.findViewById(R.id.recyclerTrips)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        tvGreeting = view.findViewById(R.id.tvGreeting)
        tvEmptyTrips = view.findViewById(R.id.tvEmptyTrips)


        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)
        val actionCreate = view.findViewById<LinearLayout>(R.id.actionCreate)
        val actionInvite = view.findViewById<LinearLayout>(R.id.actionInvite)
        //   val actionExpenses = view.findViewById<LinearLayout>(R.id.actionExpenses)
        val cardPalawan = view.findViewById<MaterialCardView>(R.id.cardPalawan)
        val cardBaguio = view.findViewById<MaterialCardView>(R.id.cardBaguio)
        val recyclerActivity = view.findViewById<RecyclerView>(R.id.recyclerActivity)

        activityAdapter = DashboardActivityAdapter()
        recyclerActivity.layoutManager = LinearLayoutManager(requireContext())
        recyclerActivity.adapter = activityAdapter

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



        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        val name = currentUser.displayName?.takeIf { it.isNotBlank() } ?: "Traveler"
        tvGreeting.text = "Good day, $name 👋"

        // Swipe refresh
        swipeRefresh.setOnRefreshListener {
            loadTrips(userId)
        }



        actionCreate.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_dashboardFragment_to_createTripFragment)
        }

        actionInvite.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_dashboardFragment_to_inviteMembersFragment)
        }

        cardPalawan.setOnClickListener {
            Toast.makeText(requireContext(), "Discover Palawan", Toast.LENGTH_SHORT).show()
        }

        cardBaguio.setOnClickListener {
            Toast.makeText(requireContext(), "Discover Baguio", Toast.LENGTH_SHORT).show()
        }

      //  actionExpenses.setOnClickListener {
       //     view.findNavController()
       //         .navigate(R.id.action_dashboardFragment_to_addExpenseFragment)
      //  }

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
        loadRecentActivity(userId)
    }

    private fun loadTrips(userId: String) {
        swipeRefresh.isRefreshing = true

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
                }.sortedByDescending { it.date }
                    .take(3)

                adapter.submitList(tripList)

                if (tripList.isEmpty()) {
                    tvEmptyTrips.visibility = View.VISIBLE
                    recyclerTrips.visibility = View.GONE
                } else {
                    tvEmptyTrips.visibility = View.GONE
                    recyclerTrips.visibility = View.VISIBLE
                }

                swipeRefresh.isRefreshing = false
            }
            .addOnFailureListener {
                swipeRefresh.isRefreshing = false
                Toast.makeText(requireContext(), "Failed to load trips", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadRecentActivity(userId: String) {

        db.collection("activities")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { result ->

                val list = result.map { doc ->

                    val title = doc.getString("title") ?: ""
                    val date = doc.getString("date") ?: ""
                    val time = doc.getString("time") ?: ""

                    DashboardActivity(
                        title = "You added activity: $title",
                        dateTime = "$date • $time"
                    )
                }

                activityAdapter.submitList(list)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load activity", Toast.LENGTH_SHORT).show()
            }
    }
}
