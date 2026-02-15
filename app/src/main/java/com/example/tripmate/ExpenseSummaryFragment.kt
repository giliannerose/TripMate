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

class ExpenseSummaryFragment : Fragment(R.layout.fragment_expense_summary) {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        adapter = ExpenseAdapter { expense ->
            viewModel.delete(expense)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvExpenses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.expenses.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        super.onViewCreated(view, savedInstanceState)

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
            findNavController()
                .navigate(R.id.action_expenseSummaryFragment_to_addExpenseFragment)
        }

        // Top tabs
        view.findViewById<Button>(R.id.tabParticipants).setOnClickListener {
            findNavController()
                .navigate(R.id.action_expenseSummaryFragment_to_tripDetailsFragment)
        }

        view.findViewById<Button>(R.id.tabPolls).setOnClickListener {
            findNavController()
                .navigate(R.id.action_expenseSummaryFragment_to_createPollFragment)
        }

        view.findViewById<Button>(R.id.tabExpenses).setOnClickListener {
            Toast.makeText(requireContext(), "You're already on Expenses", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.tabDocs).setOnClickListener {
            findNavController()
                .navigate(R.id.action_expenseSummaryFragment_to_documentsFragment)
        }

        view.findViewById<Button>(R.id.tabItinerary).setOnClickListener {
            findNavController()
                .navigate(R.id.action_expenseSummaryFragment_to_itineraryFragment)
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
