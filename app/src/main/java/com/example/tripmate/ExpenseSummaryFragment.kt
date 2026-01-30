package com.example.tripmate.ui.expense

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExpenseSummaryFragment : Fragment(R.layout.fragment_expense_summary) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddExpense = view.findViewById<Button>(R.id.btnAddExpense)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        val expenseCard = view.findViewById<CardView>(R.id.expenseCard)
        val btnMarkSettled = view.findViewById<Button>(R.id.btnMarkSettled)
        val btnDeleteExpense = view.findViewById<Button>(R.id.btnDeleteExpense)

        val expenseCard2 = view.findViewById<CardView>(R.id.expenseCard2)
        val btnMark2 = view.findViewById<Button>(R.id.btnMarkSettled2)
        val btnDelete2 = view.findViewById<Button>(R.id.btnDeleteExpense2)

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

        // Mark settled (1st)
        btnMarkSettled.setOnClickListener {
            btnMarkSettled.backgroundTintList =
                requireContext().getColorStateList(android.R.color.darker_gray)
            btnMarkSettled.text = "Settled"
            btnMarkSettled.isEnabled = false

            Toast.makeText(
                requireContext(),
                "Expense marked as settled",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Delete expense (1st)
        btnDeleteExpense.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Yes") { dialog, _ ->
                    expenseCard.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Expense deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        // Mark settled (2nd)
        btnMark2.setOnClickListener {
            btnMark2.backgroundTintList =
                requireContext().getColorStateList(android.R.color.darker_gray)
            btnMark2.text = "Settled"
            btnMark2.isEnabled = false

            Toast.makeText(
                requireContext(),
                "Expenses settled",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Delete expense (2nd)
        btnDelete2.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Yes") { dialog, _ ->
                    expenseCard2.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
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
