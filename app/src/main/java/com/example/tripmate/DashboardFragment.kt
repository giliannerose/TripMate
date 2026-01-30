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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val cardTripSiargao = view.findViewById<CardView>(R.id.cardTripSiargao)
        val btnViewItinerary = view.findViewById<Button>(R.id.btnViewItinerary)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        val actionCreate = view.findViewById<LinearLayout>(R.id.actionCreate)
        val actionInvite = view.findViewById<LinearLayout>(R.id.actionInvite)
        val actionExpenses = view.findViewById<LinearLayout>(R.id.actionExpenses)

        // Swipe refresh
        swipeRefresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Dashboard refreshed", Toast.LENGTH_SHORT).show()
            swipeRefresh.isRefreshing = false
        }

        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    Toast.makeText(requireContext(), "Single tap detected", Toast.LENGTH_SHORT)
                        .show()
                    return true
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    Toast.makeText(requireContext(), "Double tap detected", Toast.LENGTH_SHORT)
                        .show()
                    return true
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (velocityX < -1000) {
                        Toast.makeText(requireContext(), "Swipe left detected", Toast.LENGTH_SHORT)
                            .show()
                        return true
                    }
                    return false
                }
            }
        )

        cardTripSiargao.setOnTouchListener { v, event ->
            val handled = gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            handled
        }

        btnViewItinerary.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_dashboardFragment_to_itineraryFragment)
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
    }



}
