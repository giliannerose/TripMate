package com.example.tripmate.ui.notifications

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.example.tripmate.databinding.FragmentNotificationsBinding
import com.example.tripmate.ui.notification.NotificationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tripmate.data.model.NotificationEntity

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels()

    private val args: NotificationsFragmentArgs by navArgs()
    private lateinit var adapter: NotificationAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNotificationsBinding.bind(view)

        val tripId = args.tripId
        val tripTitle = args.tripTitle
        val tripDate = args.tripDate

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid


        if (tripId.isEmpty()) {
            // Opened from bottom navigation without trip
            Toast.makeText(requireContext(), "Showing general notifications", Toast.LENGTH_SHORT).show()
        }

        adapter = NotificationAdapter { notification, action ->

            when (action) {

                "ACCEPT" -> {
                    firestore.collection("notifications")
                        .document(notification.id)
                        .update("status", "ACCEPTED")
                }

                "DECLINE" -> {
                    firestore.collection("notifications")
                        .document(notification.id)
                        .update("status", "DECLINED")
                }

                "ACKNOWLEDGE" -> {
                    firestore.collection("notifications")
                        .document(notification.id)
                        .update("status", "ACKNOWLEDGED")
                }

                "VIEW_POLL" -> {
                    val action =
                        NotificationsFragmentDirections
                            .actionNotificationsFragmentToVoteFragment(
                                tripId,
                                tripTitle,
                                tripDate
                            )

                    findNavController().navigate(action)
                }

                "VIEW_EXPENSE" -> {
                    findNavController()
                    val action =
                        NotificationsFragmentDirections
                            .actionNotificationsFragmentToExpenseSummaryFragment(
                                tripId,
                                tripTitle,
                                tripDate
                            )

                    findNavController().navigate(action)
                }
            }
        }

        binding.recyclerNotifications.adapter = adapter

        firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Toast.makeText(requireContext(), "Error loading notifications", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(NotificationEntity::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                adapter.submitList(list)
            }

        // Bottom Navigation
        val bottomNav = binding.bottomNav
        bottomNav.selectedItemId = R.id.nav_notifications

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.dashboardFragment)
                }
                R.id.nav_create -> {
                    findNavController().navigate(R.id.myTripsFragment)
                }
                R.id.nav_notifications -> {
                    Toast.makeText(
                        requireContext(),
                        "You're already on Notifications",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.nav_profile -> {
                    findNavController().navigate(R.id.profileFragment)
                }
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
