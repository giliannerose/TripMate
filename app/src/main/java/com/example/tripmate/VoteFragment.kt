package com.example.tripmate.ui.polls

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tripmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.data.model.PollEntity
import com.example.tripmate.ui.poll.PollAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class VoteFragment : Fragment(R.layout.fragment_vote) {

    private lateinit var adapter: PollAdapter

    private val args: VoteFragmentArgs by navArgs()

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val tripId = args.tripId

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerPolls)

        adapter = PollAdapter { poll: PollEntity, selectedOption: String ->

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                return@PollAdapter
            }

            val userId = currentUser.uid

            // Save vote inside poll document
            db.collection("polls")
                .whereEqualTo("tripId", poll.tripId)
                .whereEqualTo("question", poll.question)
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {

                        val voteData = hashMapOf(
                            "userId" to userId,
                            "selectedOption" to selectedOption
                        )

                        db.collection("polls")
                            .document(document.id)
                            .collection("votes")
                            .add(voteData)
                    }

                    Toast.makeText(
                        requireContext(),
                        "Vote submitted: $selectedOption",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

       // viewModel.getPollsByTrip(tripId).observe(viewLifecycleOwner) { polls ->
        //    adapter.submitList(polls)
       // }'


        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        db.collection("polls")
            .whereEqualTo("tripId", tripId)
            .get()
            .addOnSuccessListener { result ->

                val pollList = mutableListOf<PollEntity>()

                for (document in result) {

                    val poll = PollEntity(
                        tripId = document.getString("tripId") ?: "",
                        question = document.getString("question") ?: "",
                        option1 = document.getString("option1") ?: "",
                        option2 = document.getString("option2") ?: "",
                        option3 = document.getString("option3"),
                        option4 = document.getString("option4")
                    )

                    pollList.add(poll)
                }

                adapter.submitList(pollList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        val btnViewResults = view.findViewById<Button>(R.id.btnViewResults)

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





        // Tabs navigation
        tabParticipants.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_tripDetailsFragment)
        }

        tabPolls.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "You're already on Polls",
                Toast.LENGTH_SHORT
            ).show()
        }

        tabExpenses.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_expenseSummaryFragment)
        }

        tabDocs.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_documentsFragment)
        }

        tabItinerary.setOnClickListener {
            findNavController()
                .navigate(R.id.action_voteFragment_to_itineraryFragment)
        }

        btnViewResults.setOnClickListener {
            val action =
                VoteFragmentDirections
                    .actionVoteFragmentToPollResultsFragment(
                        args.tripId,
                        args.tripTitle,
                        args.tripDate
                    )

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
