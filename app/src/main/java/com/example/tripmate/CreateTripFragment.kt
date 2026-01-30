package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import java.util.Calendar
import androidx.appcompat.app.AlertDialog
import android.app.DatePickerDialog



class CreateTripFragment : Fragment(R.layout.fragment_create_trip) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTripName = view.findViewById<EditText>(R.id.etTripName)
        val etDestination = view.findViewById<EditText>(R.id.etDestination)
        val etStartDate = view.findViewById<EditText>(R.id.etStartDate)
        val etEndDate = view.findViewById<EditText>(R.id.etEndDate)
        val btnStartDate = view.findViewById<ImageView>(R.id.btnStartDate)
        val btnEndDate = view.findViewById<ImageView>(R.id.btnEndDate)
        val btnSaveTrip = view.findViewById<Button>(R.id.btnSaveTrip)
        val btnInviteMembers = view.findViewById<Button>(R.id.btnInviteMembers)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        btnStartDate.setOnClickListener {
            showDatePicker(etStartDate)
        }

        btnEndDate.setOnClickListener {
            showDatePicker(etEndDate)
        }

        btnSaveTrip.setOnClickListener {
            val tripName = etTripName.text.toString().trim()
            val destination = etDestination.text.toString().trim()
            val startDate = etStartDate.text.toString().trim()
            val endDate = etEndDate.text.toString().trim()

            etTripName.error = null
            etDestination.error = null
            etStartDate.error = null
            etEndDate.error = null

            if (tripName.isEmpty()) {
                etTripName.error = "Trip name is required"
                etTripName.requestFocus()
                return@setOnClickListener
            }

            if (destination.isEmpty()) {
                etDestination.error = "Destination is required"
                etDestination.requestFocus()
                return@setOnClickListener
            }

            if (startDate.isEmpty()) {
                etStartDate.error = "Start date is required"
                etStartDate.requestFocus()
                return@setOnClickListener
            }

            if (endDate.isEmpty()) {
                etEndDate.error = "End date is required"
                etEndDate.requestFocus()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Save Trip")
                .setMessage("Are you sure you want to save this trip?")
                .setPositiveButton("Save") { dialog, _ ->
                    Toast.makeText(requireContext(), "Trip saved successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    // navigation unchanged (as per your comment)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        btnInviteMembers.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_createTripFragment_to_inviteMembersFragment)
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
                    view.findNavController().navigate(R.id.dashboardFragment)

                R.id.nav_create ->
                    view.findNavController().navigate(R.id.myTripsFragment)

                R.id.nav_notifications ->
                    view.findNavController().navigate(R.id.notificationsFragment)

                R.id.nav_profile ->
                    view.findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }


    private fun showDatePicker(targetEditText: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "${dayOfMonth}/${month + 1}/$year"
                targetEditText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }


}
