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

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNotificationsBinding.bind(view)

        adapter = NotificationAdapter { notification, action ->

            when (action) {

                "ACCEPT" -> {
                    viewModel.update(notification.copy(status = "ACCEPTED"))
                }

                "DECLINE" -> {
                    viewModel.update(notification.copy(status = "DECLINED"))
                }

                "ACKNOWLEDGE" -> {
                    viewModel.update(notification.copy(status = "ACKNOWLEDGED"))
                }

                "VIEW_POLL" -> {
                    findNavController()
                        .navigate(R.id.action_notificationsFragment_to_voteFragment)
                }

                "VIEW_EXPENSE" -> {
                    findNavController()
                        .navigate(R.id.action_notificationsFragment_to_expenseSummaryFragment)
                }
            }
        }

        binding.recyclerNotifications.adapter = adapter

        val tripId = 1L

        viewModel.getNotifications(tripId)
            .observe(viewLifecycleOwner) { list ->
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
