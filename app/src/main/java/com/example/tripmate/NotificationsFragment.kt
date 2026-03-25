package com.example.tripmate.ui.notifications

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripmate.R
import com.example.tripmate.data.model.NotificationEntity
import com.example.tripmate.databinding.FragmentNotificationsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val args: NotificationsFragmentArgs by navArgs()
    private lateinit var adapter: NotificationAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var notificationListener: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationsBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        setupRecyclerView()
        setupAdapter()
        setupButtons(userId)
        setupBottomNavigation()
        loadNotifications(userId)
    }

    private fun setupRecyclerView() {
        binding.recyclerNotifications.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAdapter() {
        adapter = NotificationAdapter { notification, action ->
            when (action) {
                "ACCEPT" -> updateNotificationStatus(notification.id, "ACCEPTED")
                "DECLINE" -> updateNotificationStatus(notification.id, "DECLINED")
                "ACKNOWLEDGE" -> updateNotificationStatus(notification.id, "ACKNOWLEDGED")
                "MARK_READ" -> markAsRead(notification.id)
                "VIEW_POLL" -> {
                    val actionNav = NotificationsFragmentDirections
                        .actionNotificationsFragmentToVoteFragment(
                            notification.tripId,
                            notification.tripTitle,
                            notification.tripDate
                        )
                    findNavController().navigate(actionNav)
                }
                "VIEW_EXPENSE" -> {
                    val actionNav = NotificationsFragmentDirections
                        .actionNotificationsFragmentToExpenseSummaryFragment(
                            notification.tripId,
                            notification.tripTitle,
                            notification.tripDate
                        )
                    findNavController().navigate(actionNav)
                }
            }
        }

        binding.recyclerNotifications.adapter = adapter
    }

    private fun setupButtons(userId: String) {
        binding.btnMarkAllRead.setOnClickListener {
            firestore.collection("notifications")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val batch = firestore.batch()

                    for (doc in snapshot.documents) {
                        batch.update(doc.reference, "isRead", true)
                    }

                    batch.commit()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "All notifications marked as read", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to update notifications", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }

    private fun loadNotifications(userId: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtEmptyState.visibility = View.GONE
        binding.recyclerNotifications.visibility = View.GONE

        notificationListener = firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->

                binding.progressBar.visibility = View.GONE

                if (error != null) {
                    Toast.makeText(requireContext(), "Error loading notifications", Toast.LENGTH_SHORT).show()
                    binding.txtEmptyState.visibility = View.VISIBLE
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(NotificationEntity::class.java)?.copy(id = doc.id)
                }?.sortedByDescending { it.createdAt } ?: emptyList()

                adapter.submitList(list)

                if (list.isEmpty()) {
                    binding.txtEmptyState.visibility = View.VISIBLE
                    binding.recyclerNotifications.visibility = View.GONE
                } else {
                    binding.txtEmptyState.visibility = View.GONE
                    binding.recyclerNotifications.visibility = View.VISIBLE
                }
            }
    }

    private fun updateNotificationStatus(notificationId: String, status: String) {
        firestore.collection("notifications")
            .document(notificationId)
            .update(
                mapOf(
                    "status" to status,
                    "isRead" to true
                )
            )
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update notification", Toast.LENGTH_SHORT).show()
            }
    }

    private fun markAsRead(notificationId: String) {
        firestore.collection("notifications")
            .document(notificationId)
            .update("isRead", true)
    }

    private fun setupBottomNavigation() {
        val bottomNav = binding.bottomNav
        bottomNav.selectedItemId = R.id.nav_notifications

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> findNavController().navigate(R.id.dashboardFragment)
                R.id.nav_create -> findNavController().navigate(R.id.myTripsFragment)
                R.id.nav_notifications -> {
                    Toast.makeText(requireContext(), "You're already on Notifications", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        notificationListener?.remove()
        _binding = null
    }
}