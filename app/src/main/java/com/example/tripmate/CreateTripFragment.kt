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
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.ui.trip.TripViewModel

import com.example.tripmate.data.remote.FirebaseStoreManager
import com.google.firebase.auth.FirebaseAuth




class CreateTripFragment : Fragment(R.layout.fragment_create_trip) {

        private var startDateCalendar: Calendar? = null
        private var endDateCalendar: Calendar? = null
        private lateinit var viewModel: TripViewModel
        private val firebaseStoreManager = FirebaseStoreManager()
    private val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TripViewModel::class.java]

        val etTripName = view.findViewById<EditText>(R.id.etTripName)
        val etDestination = view.findViewById<EditText>(R.id.etDestination)
        val etStartDate = view.findViewById<EditText>(R.id.etStartDate)
        val etEndDate = view.findViewById<EditText>(R.id.etEndDate)
        val btnStartDate = view.findViewById<ImageView>(R.id.btnStartDate)
        val btnEndDate = view.findViewById<ImageView>(R.id.btnEndDate)
        val etNotes = view.findViewById<EditText>(R.id.etNotes)

        val btnSaveTrip = view.findViewById<Button>(R.id.btnSaveTrip)
        val btnInviteMembers = view.findViewById<Button>(R.id.btnInviteMembers)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        val spCountry = view.findViewById<Spinner>(R.id.spCountry)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCountry.adapter = adapter

        btnStartDate.setOnClickListener {
            showDatePicker(etStartDate, true)
        }

        btnEndDate.setOnClickListener {
            showDatePicker(etEndDate, false)
        }


        btnSaveTrip.setOnClickListener {
            val tripName = etTripName.text.toString().trim()
            val destination = etDestination.text.toString().trim()
            val startDate = etStartDate.text.toString().trim()
            val endDate = etEndDate.text.toString().trim()
            val country = spCountry.selectedItem.toString()
            val notes = etNotes.text.toString().trim()

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

            if (startDateCalendar != null && endDateCalendar != null) {
                if (endDateCalendar!!.before(startDateCalendar)) {
                    etEndDate.error = "End date cannot be earlier than start date"
                    etEndDate.requestFocus()
                    return@setOnClickListener
                }
            }


            AlertDialog.Builder(requireContext())
                .setTitle("Save Trip")
                .setMessage("Are you sure you want to save this trip?")
                .setPositiveButton("Save") { dialog, _ ->

                    // Create the Trip object


                    val userId = currentUserId ?: ""

                    val trip = TripEntity(
                        userId = userId,
                        name = tripName,
                        description = destination,
                        date = "$startDate - $endDate",
                        country = country
                    )



                    // Save to Local DB (Room/ViewModel)
                    viewModel.insert(trip)

                    // Save to Firebase
                    firebaseStoreManager.saveTrip(
                        trip = trip,
                        userId = userId
                    ) { success ->
                        if (success) {
                            Log.d("TRIPMATE_DEBUG", "Firebase Sync Successful")
                        } else {
                            Log.e("TRIPMATE_DEBUG", "Firebase Sync Failed")
                        }
                    }

                    Toast.makeText(requireContext(), "Trip saved successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

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


    private fun showDatePicker(
        targetEditText: EditText,
        isStartDate: Boolean
    ) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }

                val selectedDate = "${dayOfMonth}/${month + 1}/$year"
                targetEditText.setText(selectedDate)

                if (isStartDate) {
                    startDateCalendar = selectedCalendar
                    endDateCalendar = null
                    view?.findViewById<EditText>(R.id.etEndDate)?.setText("")
                } else {
                    endDateCalendar = selectedCalendar
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        //  start date - end date makes sense
        if (!isStartDate && startDateCalendar != null) {
            datePicker.datePicker.minDate = startDateCalendar!!.timeInMillis
        }

        datePicker.show()
    }



}
