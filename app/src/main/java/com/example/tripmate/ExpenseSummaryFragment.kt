package com.example.tripmate.ui.expense

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tripmate.data.model.ExpenseEntity

class ExpenseSummaryFragment : Fragment(R.layout.fragment_expense_summary) {


    private lateinit var adapter: ExpenseAdapter

    private val args: ExpenseSummaryFragmentArgs by navArgs()
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

        adapter = ExpenseAdapter { expense ->

            db.collection("expenses")
                .document(expense.firestoreId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
                }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvExpenses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        db.collection("expenses")
            .whereEqualTo("userId", userId)
            .whereEqualTo("tripId", tripId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val expenseList = mutableListOf<ExpenseEntity>()

                snapshot?.documents?.forEach { doc ->
                    val expense = doc.toObject(ExpenseEntity::class.java)
                    expense?.firestoreId = doc.id
                    if (expense != null) {
                        expenseList.add(expense)
                    }
                }

                adapter.submitList(expenseList)
            }

        val btnAddExpense = view.findViewById<Button>(R.id.btnAddExpense)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)


        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        // Add expense
        btnAddExpense.setOnClickListener {
            val action =
                ExpenseSummaryFragmentDirections
                    .actionExpenseSummaryFragmentToAddExpenseFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        // Top tabs
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {

            val action =
                ExpenseSummaryFragmentDirections
                    .actionExpenseSummaryFragmentToTripDetailsFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }


        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {
            val action =
                ExpenseSummaryFragmentDirections
                    .actionExpenseSummaryFragmentToCreatePollFragment( tripId,
                        tripTitle,
                        tripDate)

            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            Toast.makeText(requireContext(), "You're already on Expenses", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {
            val action =
                ExpenseSummaryFragmentDirections
                    .actionExpenseSummaryFragmentToDocumentsFragment( tripId,
                        tripTitle,
                        tripDate)


            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            val action =
                ExpenseSummaryFragmentDirections
                    .actionExpenseSummaryFragmentToItineraryFragment( tripId,
                        tripTitle,
                        tripDate)


            findNavController().navigate(action)
        }



        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    findNavController().navigate(R.id.dashboardFragment)

                R.id.nav_create ->
                    findNavController().navigate(R.id.myTripsFragment)

                R.id.nav_notifications ->
                    findNavController().navigate(R.id.notificationsFragment)

                R.id.nav_profile ->
                    findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }
}
