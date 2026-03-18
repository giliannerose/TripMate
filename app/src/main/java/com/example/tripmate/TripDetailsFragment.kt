package com.example.tripmate.ui.tripdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import com.example.tripmate.databinding.FragmentTripDetailsBinding
import com.example.tripmate.ui.trip.ParticipantAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tripmate.data.model.UserEntity



class TripDetailsFragment : Fragment() {

    private lateinit var adapter: ParticipantAdapter

    private var _binding: FragmentTripDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: TripDetailsFragmentArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tripId = args.tripId
        val tripTitle = args.tripTitle
        val tripDate = args.tripDate

        binding.tvTripTitle.text = tripTitle
        binding.tvTripDate.text = tripDate


        adapter = ParticipantAdapter(
            onDelete = { user ->
                db.collection("trips")
                    .document(tripId)
                    .collection("participants")
                    .document(user.id.toString())
                    .delete()
            },
            onEdit = { user ->
                showEditDialog(user)
            }
        )

        binding.participantsRecyclerView.adapter = adapter
        binding.participantsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        db.collection("trips")
            .document(tripId)
            .collection("participants")
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<UserEntity>()

                for (doc in result) {
                    val user = doc.toObject(UserEntity::class.java)
                    user.id = doc.id
                    list.add(user)
                }

                adapter.submitList(list)
            }

        binding.tabParticipants
        val tabPolls = view.findViewById<Button>(R.id.tabPolls)
        val tabExpenses = view.findViewById<Button>(R.id.tabExpenses)
        val tabDocs = view.findViewById<Button>(R.id.tabDocs)
        val tabItinerary = view.findViewById<Button>(R.id.tabItinerary)
        val btnAddParticipant = view.findViewById<Button>(R.id.btnAddParticipant)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)


        // Remove blue highlight in bottom nav
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }
        bottomNav.menu.setGroupCheckable(0, true, true)




        // Top tabs
        binding.tabParticipants.setOnClickListener{
            Toast.makeText(requireContext(), "You're in Participants", Toast.LENGTH_SHORT).show()
        }

        tabPolls.setOnClickListener {
            val action =
                TripDetailsFragmentDirections
                    .actionTripDetailsFragmentToCreatePollFragment( tripId,
                        tripTitle,
                        tripDate)

            findNavController().navigate(action)
        }

        tabExpenses.setOnClickListener {
            val action =
                TripDetailsFragmentDirections
                    .actionTripDetailsFragmentToExpenseSummaryFragment( tripId,
                        tripTitle,
                        tripDate)

            findNavController().navigate(action)
        }

        tabDocs.setOnClickListener {
            val action =
                TripDetailsFragmentDirections
                    .actionTripDetailsFragmentToDocumentsFragment( tripId,
                        tripTitle,
                        tripDate)


            findNavController().navigate(action)
        }

        tabItinerary.setOnClickListener {
            val action =
                TripDetailsFragmentDirections
                    .actionTripDetailsFragmentToItineraryFragment( tripId,
                        tripTitle,
                        tripDate)


            findNavController().navigate(action)
        }

        binding.btnAddParticipant.setOnClickListener {
            showAddParticipantDialog(tripId)
        }

        // Bottom navigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    findNavController().navigate(R.id.dashboardFragment)

                R.id.nav_create ->
                    findNavController().navigate(R.id.myTripsFragment)

                R.id.nav_notifications -> {
                    val action =
                        TripDetailsFragmentDirections
                            .actionTripDetailsFragmentToNotificationsFragment(
                                tripId,
                                tripTitle,
                                tripDate
                            )

                    findNavController().navigate(action)
                }

                R.id.nav_profile ->
                    findNavController().navigate(R.id.profileFragment)
            }
            true
        }
    }

    private fun showEditDialog(user: UserEntity) {

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val etName = EditText(requireContext()).apply {
            hint = "Name"
            setText(user.name)
        }

        val etEmail = EditText(requireContext()).apply {
            hint = "Email"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setText(user.email)
        }

        layout.addView(etName)
        layout.addView(etEmail)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Participant")
            .setView(layout)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                val newName = etName.text.toString().trim()
                val newEmail = etEmail.text.toString().trim()

                // Clear previous errors
                etName.error = null
                etEmail.error = null

                when {
                    newName.isEmpty() -> {
                        etName.error = "Name cannot be empty"
                    }

                    newEmail.isEmpty() -> {
                        etEmail.error = "Email cannot be empty"
                    }

                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(newEmail)
                        .matches() -> {

                        etEmail.error = "Invalid email format"
                    }

                    else -> {
                        val updatedUser = user.copy(
                            name = newName,
                            email = newEmail
                        )

                        db.collection("trips")
                            .document(args.tripId)
                            .collection("participants")
                            .document(user.id.toString())
                            .set(updatedUser)
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddParticipantDialog (tripId: String){

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val etName = EditText(requireContext()).apply {
            hint = "Name"
        }

        val etEmail = EditText(requireContext()).apply {
            hint = "Email"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        layout.addView(etName)
        layout.addView(etEmail)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Participant")
            .setView(layout)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()

                etName.error = null
                etEmail.error = null

                when {
                    name.isEmpty() -> {
                        etName.error = "Name cannot be empty"
                    }

                    email.isEmpty() -> {
                        etEmail.error = "Email cannot be empty"
                    }

                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches() -> {

                        etEmail.error = "Invalid email format"
                    }

                    else -> {

                        val newUser = UserEntity(
                            name = name,
                            email = email,
                            passwordHash = ""
                        )

                        db.collection("trips")
                            .document(tripId)
                            .collection("participants")
                            .add(newUser)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Participant added", Toast.LENGTH_SHORT).show()

                                // reload
                                onViewCreated(requireView(), null)
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to add", Toast.LENGTH_SHORT).show()
                            }

                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

}
