    package com.example.tripmate

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.LinearLayout
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.google.android.material.bottomnavigation.BottomNavigationView

    class ProfileActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_profile)

            val btnEditProfile = findViewById<Button>(R.id.btnEditProfile)
            val btnSettings = findViewById<Button>(R.id.btnSettings)

            val btnSignOut = findViewById<Button>(R.id.btnSignOut)
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

            //  Highlight the Profile icon
            bottomNav.selectedItemId = R.id.nav_profile

            btnEditProfile.setOnClickListener {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }

            btnSettings.setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }

            btnSignOut.setOnClickListener {
                // confirmation dialog
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()

                        // Route to Welcome page
                        val intent = Intent(this, WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish() //
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            //highlight profile
            bottomNav.selectedItemId = R.id.nav_profile

            // Bottom navigation
            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.nav_create -> {
                        val intent = Intent(this, MyTripsActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.nav_notifications -> {
                        val intent = Intent(this, NotificationsActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.nav_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
    }
