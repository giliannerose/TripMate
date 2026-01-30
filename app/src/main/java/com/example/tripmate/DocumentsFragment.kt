package com.example.tripmate.ui.documents

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private val PICK_FILE_REQUEST = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnUpload = view.findViewById<Button>(R.id.btnUpload)
        val tabParticipants = view.findViewById<Button>(R.id.tabParticipants)
        val tabPolls = view.findViewById<Button>(R.id.tabPolls)
        val tabExpenses = view.findViewById<Button>(R.id.tabExpenses)
        val tabDocs = view.findViewById<Button>(R.id.tabDocs)
        val tabItinerary = view.findViewById<Button>(R.id.tabItinerary)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)

        // remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)

        // Upload button (system intent stays)
        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(
                Intent.createChooser(intent, "Select a file to upload"),
                PICK_FILE_REQUEST
            )
        }

        // Tabs navigation
        tabParticipants.setOnClickListener {
            findNavController()
                .navigate(R.id.action_documentsFragment_to_tripDetailsFragment)
        }

        tabPolls.setOnClickListener {
            findNavController()
                .navigate(R.id.action_documentsFragment_to_createPollFragment)
        }

        tabExpenses.setOnClickListener {
            findNavController()
                .navigate(R.id.action_documentsFragment_to_expenseSummaryFragment)
        }

        tabDocs.setOnClickListener {
            Toast.makeText(requireContext(), "You're already on Docs", Toast.LENGTH_SHORT).show()
        }

        tabItinerary.setOnClickListener {
            findNavController()
                .navigate(R.id.action_documentsFragment_to_itineraryFragment)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            val fileUri: Uri? = data?.data

            if (fileUri != null) {
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setMessage("Uploading file...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                Handler().postDelayed({
                    progressDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "File uploaded successfully!",
                        Toast.LENGTH_LONG
                    ).show()
                }, 2000)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No file selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
