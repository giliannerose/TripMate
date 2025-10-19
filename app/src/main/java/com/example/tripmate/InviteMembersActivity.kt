package com.example.tripmate

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class InviteMembersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_members)

        val btnSendInvite = findViewById<Button>(R.id.btnSendInvite)
        val cbEva = findViewById<CheckBox>(R.id.cbEva)
        val cbAnna = findViewById<CheckBox>(R.id.cbAnna)
        val cbMonica = findViewById<CheckBox>(R.id.cbMonica)
        val cbDenise = findViewById<CheckBox>(R.id.cbDenise)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Send Invite
        btnSendInvite.setOnClickListener {
            val selected = mutableListOf<String>()
            if (cbEva.isChecked) selected.add("Eva")
            if (cbAnna.isChecked) selected.add("Anna Marie")
            if (cbMonica.isChecked) selected.add("Monica")
            if (cbDenise.isChecked) selected.add("Denise")

            if (selected.isEmpty()) {
                Toast.makeText(this, "Please select at least one member.", Toast.LENGTH_SHORT).show()
            } else {
                //  confirmation dialog
                val selectedMembers = selected.joinToString(", ")
                val message = "Are you sure you want to send invites to: $selectedMembers?"

                com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                    .setTitle("Confirm Invitation")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                        // User confirmed
                        Toast.makeText(this, "Invites sent to: $selectedMembers", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        // User cancelled
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
        //----------

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, DashboardActivity::class.java))
                R.id.nav_create -> startActivity(Intent(this, MyTripsActivity::class.java))
                R.id.nav_notifications -> startActivity(Intent(this, NotificationsActivity::class.java))
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            overridePendingTransition(0, 0)
            true
        }
    }
}
