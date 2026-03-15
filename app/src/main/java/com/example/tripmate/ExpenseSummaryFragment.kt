package com.example.tripmate.ui.expense

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.R
import com.example.tripmate.ui.itinerary.ExpenseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.navigation.fragment.navArgs

class ExpenseSummaryFragment : Fragment(R.layout.fragment_expense_summary) {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    private val args: ExpenseSummaryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        val tripId = args.tripId
        val tripTitle = args.tripTitle
        val tripDate = args.tripDate

        adapter = ExpenseAdapter { expense ->
            viewModel.delete(expense)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvExpenses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.expenses.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
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
                    .actionExpenseSummaryFragmentToAddExpenseFragment(tripId)

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
