package com.example.tripmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import android.app.DatePickerDialog
import com.example.tripmate.data.model.ExpenseEntity
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {


    private val args: AddExpenseFragmentArgs by navArgs()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val tripId = args.tripId
        val tripTitle = args.tripTitle
        val tripDate = args.tripDate

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = user.uid

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etAmount = view.findViewById<EditText>(R.id.etAmount)
        val etNotes = view.findViewById<EditText>(R.id.etNotes)

        val cbAlice = view.findViewById<CheckBox>(R.id.cbAlice)
        val cbBob = view.findViewById<CheckBox>(R.id.cbBob)
        val cbJane = view.findViewById<CheckBox>(R.id.cbJane)

        val etDate = view.findViewById<EditText>(R.id.etDate)
        val btnPickDate = view.findViewById<ImageView>(R.id.btnPickDate)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnViewSummary = view.findViewById<Button>(R.id.btnViewSummary)
        val btnUpload = view.findViewById<Button>(R.id.btnUpload)
        val tvFileChosen = view.findViewById<TextView>(R.id.tvFileChosen)
        val spCategory = view.findViewById<Spinner>(R.id.spCategory)
        val spPaidBy = view.findViewById<Spinner>(R.id.spPaidBy)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        // Dropdowns
        val categories = arrayOf("Food", "Transport", "Accommodation", "Activity", "Other")
        spCategory.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )

        val members = arrayOf("Jane Doe", "Alice", "Bob", "You")
        spPaidBy.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, members)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        // Date Picker
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    etDate.setText("$day/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Upload placeholder
        btnUpload.setOnClickListener {
            tvFileChosen.text = "receipt.jpg"
            Toast.makeText(requireContext(), "Simulated upload complete", Toast.LENGTH_SHORT).show()
        }

        // Save Button
        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val amountText = etAmount.text.toString().trim()
            val date = etDate.text.toString().trim()
            val notes = etNotes.text.toString().trim()
            val category = spCategory.selectedItem.toString()
            val paidBy = spPaidBy.selectedItem.toString()

            etTitle.error = null
            etAmount.error = null
            etDate.error = null

            if (title.isEmpty()) {
                etTitle.error = "Expense title is required"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            if (amountText.isEmpty()) {
                etAmount.error = "Amount is required"
                etAmount.requestFocus()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                etAmount.error = "Enter a valid amount"
                etAmount.requestFocus()
                return@setOnClickListener
            }

            if (date.isEmpty()) {
                etDate.error = "Date is required"
                etDate.requestFocus()
                return@setOnClickListener
            }

            if (!cbAlice.isChecked && !cbBob.isChecked && !cbJane.isChecked) {
                Toast.makeText(
                    requireContext(),
                    "Select at least one person to split with",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Save Expense")
                .setMessage("Do you want to save this expense and go to the summary?")
                .setPositiveButton("Yes") { dialog: android.content.DialogInterface, _: Int ->

                    val expense = ExpenseEntity(
                        userId = userId,
                        tripId = tripId,
                        title = title,
                        amount = amount,
                        date = date,
                        notes = notes,
                        category = category,
                        paidBy = paidBy
                    )

                    db.collection("expenses")
                        .add(expense)
                        .addOnSuccessListener { document ->

                            db.collection("expenses")
                                .document(document.id)
                                .update("firestoreId", document.id)

                            Toast.makeText(
                                requireContext(),
                                "Expense saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val action =
                                AddExpenseFragmentDirections
                                    .actionAddExpenseFragmentToExpenseSummaryFragment(
                                        tripId,
                                        tripTitle,
                                        tripDate
                                    )

                            view.findNavController().navigate(action)
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Failed to save expense",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .setNegativeButton("Cancel") { dialog: android.content.DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .show()

        btnViewSummary.setOnClickListener {
            val action =
                AddExpenseFragmentDirections
                    .actionAddExpenseFragmentToExpenseSummaryFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            view.findNavController().navigate(action)
        }

        // Top Tabs
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            view.findNavController().navigate(R.id.tripDetailsFragment)
        }

        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {
            view.findNavController().navigate(R.id.createPollFragment)
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {
            view.findNavController().navigate(R.id.documentsFragment)
        }

        view.findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            view.findNavController().navigate(R.id.itineraryFragment)
        }

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> view.findNavController().navigate(R.id.dashboardFragment)
                R.id.nav_create -> view.findNavController().navigate(R.id.myTripsFragment)
                R.id.nav_notifications -> view.findNavController().navigate(R.id.notificationsFragment)
                R.id.nav_profile -> view.findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }


    }
}