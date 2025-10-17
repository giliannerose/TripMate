package com.example.tripmate

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
                Toast.makeText(this, "Invites sent to: ${selected.joinToString(", ")}", Toast.LENGTH_LONG).show()
            }
        }

        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                R.id.nav_create -> Toast.makeText(this, "Create Trip", Toast.LENGTH_SHORT).show()
                R.id.nav_notifications -> Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}
