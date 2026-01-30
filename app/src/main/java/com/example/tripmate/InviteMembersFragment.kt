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

class InviteMembersFragment : Fragment(R.layout.fragment_invite_members) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSendInvite = view.findViewById<Button>(R.id.btnSendInvite)
        val cbEva = view.findViewById<CheckBox>(R.id.cbEva)
        val cbAnna = view.findViewById<CheckBox>(R.id.cbAnna)
        val cbMonica = view.findViewById<CheckBox>(R.id.cbMonica)
        val cbDenise = view.findViewById<CheckBox>(R.id.cbDenise)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        btnSendInvite.setOnClickListener {
            val selected = mutableListOf<String>()
            if (cbEva.isChecked) selected.add("Eva")
            if (cbAnna.isChecked) selected.add("Anna Marie")
            if (cbMonica.isChecked) selected.add("Monica")
            if (cbDenise.isChecked) selected.add("Denise")

            if (selected.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select at least one member.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val selectedMembers = selected.joinToString(", ")
                val message = "Are you sure you want to send invites to: $selectedMembers?"

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirm Invitation")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                        Toast.makeText(
                            requireContext(),
                            "Invites sent to: $selectedMembers",
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
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

