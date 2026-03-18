
package com.example.tripmate
    import androidx.lifecycle.ViewModelProvider
    import com.example.tripmate.databinding.FragmentProfileBinding
    import com.example.tripmate.ui.user.UserViewModel
    import com.example.tripmate.data.utils.SessionManager
    import android.net.Uri
    import android.os.Bundle
    import android.view.View
    import android.widget.Button
    import android.widget.ImageView
    import android.widget.Toast
    import androidx.appcompat.app.AlertDialog
    import androidx.fragment.app.Fragment
    import androidx.navigation.fragment.findNavController
    import com.example.tripmate.R
    import com.example.tripmate.ui.trip.TripViewModel
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import com.example.tripmate.data.model.UserEntity
    import com.example.tripmate.ui.trip.TripParticipantViewModel
    import java.text.SimpleDateFormat
    import java.util.Date
    import java.util.Locale
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore

    class ProfileFragment : Fragment(R.layout.fragment_profile) {

        private lateinit var tripParticipantViewModel: TripParticipantViewModel

        private lateinit var binding: FragmentProfileBinding
        private lateinit var userViewModel: UserViewModel
        private lateinit var sessionManager: SessionManager

        private lateinit var tripViewModel: TripViewModel

        private lateinit var auth: FirebaseAuth
        private lateinit var db: FirebaseFirestore

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()

            binding = FragmentProfileBinding.bind(view)
            userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
            sessionManager = SessionManager(requireContext())
            tripViewModel = ViewModelProvider(this)[TripViewModel::class.java]
            tripParticipantViewModel = ViewModelProvider(this)[TripParticipantViewModel::class.java]


            observeUser()
            observeTripCount()
            observeCountriesVisited()
            setupClickListeners()
            setupBottomNav()
            observeBuddyCount()
            observeTripsJoined()
        }

        private fun observeUser() {

            val currentUser = auth.currentUser

            if (currentUser == null) {
                findNavController().navigate(R.id.welcomeFragment)
                return
            }

            val userId = currentUser.uid

            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->

                    if (document.exists()) {

                        val user = UserEntity(
                            id = userId,
                            name = document.getString("name") ?: "",
                            bio = document.getString("bio") ?: "",
                            gender = document.getString("gender") ?: "",
                            region = document.getString("region") ?: "",
                            age = document.getLong("age")?.toInt() ?: 0,
                            profileImageUri = document.getString("profileImageUri"),
                            createdAt = document.getLong("createdAt") ?: 0L
                        )

                        bindUser(user)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
        }

        private fun bindUser(user: UserEntity) {

            binding.tvUserName.text = user.name
            binding.tvUserBio.text = user.bio

            if (!user.profileImageUri.isNullOrEmpty()) {
                try {
                    binding.imgProfile.setImageURI(Uri.parse(user.profileImageUri))
                } catch (e: SecurityException) {
                    binding.imgProfile.setImageResource(R.drawable.ic_default_avatar)
                }
            } else {
                binding.imgProfile.setImageResource(R.drawable.ic_default_avatar)
            }

            binding.tvGender.text =
                if (user.gender.isEmpty()) "-" else user.gender

            binding.tvRegion.text =
                if (user.region.isEmpty()) "-" else "${user.region} based"

            binding.tvAge.text =
                if (user.age == 0) "-" else "${user.age} years old"


            val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            val memberSince = formatter.format(Date(user.createdAt))

            binding.tvMemberSince.text = memberSince
        }


        private fun showSignOutDialog() {
            AlertDialog.Builder(requireContext())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes") { _, _ ->

                    auth.signOut()

                    Toast.makeText(
                        requireContext(),
                        "Signed out successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController()
                        .navigate(R.id.action_profileFragment_to_welcomeFragment)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        private fun observeTripCount() {

            val userId = auth.currentUser?.uid ?: return

            tripViewModel.getTripCount(userId).observe(viewLifecycleOwner) { count ->
                binding.tvTripsCount.text = "🏝️ Trips\n$count"
            }
        }

        private fun observeBuddyCount() {

            val userId = auth.currentUser?.uid ?: return


            tripParticipantViewModel.getBuddyCount(userId)
                .observe(viewLifecycleOwner) { count ->
                    binding.tvBuddyCount.text = "👥 Buddies\n$count"
                    binding.tvBuddyCountDetails.text = count.toString()
                }
        }

        private fun observeTripsJoined() {

            val userId = auth.currentUser?.uid ?: return

            tripParticipantViewModel.getTripsJoinedCount(userId)
                .observe(viewLifecycleOwner) { count ->
                    binding.tvTripsJoined.text = count.toString()
                }
        }

        private fun observeCountriesVisited() {

            val userId = auth.currentUser?.uid ?: return

            tripViewModel.getCountriesVisited(userId.toString())
                .observe(viewLifecycleOwner) { count ->
                    binding.tvCountriesVisited.text = "🌍 Countries\n$count"
                    binding.tvCountriesVisitedDetails.text = count.toString()
                }
        }

        private fun setupBottomNav() {

            binding.bottomNav.selectedItemId = R.id.nav_profile

            binding.bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        findNavController().navigate(R.id.dashboardFragment)
                        true
                    }

                    R.id.nav_create -> {
                        findNavController().navigate(R.id.myTripsFragment)
                        true
                    }

                    R.id.nav_notifications -> {
                        findNavController().navigate(R.id.notificationsFragment)
                        true
                    }

                    R.id.nav_profile -> true
                    else -> false
                }
            }
        }

        private fun setupClickListeners() {

            binding.btnEditProfile.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_profileFragment_to_editProfileFragment)
            }

            binding.btnSettings.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_profileFragment_to_settingsFragment)
            }

            binding.btnSignOut.setOnClickListener {
                showSignOutDialog()
            }
        }

    }
