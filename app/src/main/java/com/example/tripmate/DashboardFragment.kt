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

        val tapHandler = android.os.Handler(android.os.Looper.getMainLooper())
        var singleTapRunnable: Runnable? = null


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
                    singleTapRunnable = Runnable {
                        view.findNavController()
                            .navigate(R.id.action_dashboardFragment_to_myTripsFragment)
                    }
                    tapHandler.postDelayed(singleTapRunnable!!, 250)
                    return true
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    singleTapRunnable?.let { tapHandler.removeCallbacks(it) }

                    Toast.makeText(requireContext(), "Double tap detected", Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (e1 == null) return false

                    val diffX = e2.x - e1.x
                    val diffY = e2.y - e1.y

                    if (kotlin.math.abs(diffX) > kotlin.math.abs(diffY)) {
                        if (diffX < -150) {
                            Toast.makeText(requireContext(), "Swipe left detected", Toast.LENGTH_SHORT).show()
                            return true
                        }
                    }
                    return false
                }
            }
        )


        cardTripSiargao.setOnTouchListener { v, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    swipeRefresh.isEnabled = false
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    swipeRefresh.isEnabled = true
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            gestureDetector.onTouchEvent(event)
            true
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
