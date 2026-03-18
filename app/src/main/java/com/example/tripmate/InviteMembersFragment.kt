package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.ui.user.UserViewModel
import com.example.tripmate.ui.trip.TripParticipantViewModel
import com.example.tripmate.data.model.TripParticipantEntity
import com.example.tripmate.ui.invite.InviteMembersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InviteMembersFragment : Fragment(R.layout.fragment_invite_members) {

    private val userViewModel: UserViewModel by viewModels()
    private val participantViewModel: TripParticipantViewModel by viewModels()

    private lateinit var adapter: InviteMembersAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val tripId: Long by lazy {
        requireArguments().getLong("tripId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val btnSendInvite = view.findViewById<Button>(R.id.btnSendInvite)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        adapter = InviteMembersAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerUsers)
        recyclerView.adapter = adapter

        userViewModel.allUsers.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }



        btnSendInvite.setOnClickListener {

            val selectedUsers = adapter.getSelectedUsers()

            if (selectedUsers.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select at least one member.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val names = selectedUsers.joinToString(", ") { it.name }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirm Invitation")
                .setMessage("Are you sure you want to add: $names?")
                .setPositiveButton("Yes") { dialog, _ ->


                    val currentUser = auth.currentUser
                    val currentUserId = currentUser?.uid

                    if (currentUserId == null) {
                        Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    selectedUsers.forEach { user ->

                        val participantData = hashMapOf(
                            "tripId" to tripId,
                            "userId" to user.id,
                            "addedBy" to currentUserId
                        )

                        db.collection("tripParticipants")
                            .add(participantData)
                    }

                    Toast.makeText(
                        requireContext(),
                        "Members added successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    view.findNavController()
                        .navigate(R.id.dashboardFragment)

                R.id.nav_create ->
                    view.findNavController()
                        .navigate(R.id.myTripsFragment)

                R.id.nav_notifications ->
                    view.findNavController()
                        .navigate(R.id.notificationsFragment)

                R.id.nav_profile ->
                    view.findNavController()
                        .navigate(R.id.profileFragment)
            }
            true
        }
    }


}

