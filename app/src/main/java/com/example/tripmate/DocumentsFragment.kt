package com.example.tripmate.ui.documents

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripmate.R
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.DocumentEntity
import com.example.tripmate.data.repository.DocumentRepository
import com.example.tripmate.databinding.FragmentDocumentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private var _binding: FragmentDocumentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DocumentAdapter


    private val args: DocumentsFragmentArgs by navArgs()
    private var tripId: String = ""
    private var tripTitle: String = ""
    private var tripDate: String = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var userId: String = ""



    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { handleFileSelected(it) }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDocumentsBinding.bind(view)

        tripId = args.tripId.toString()
        tripTitle = args.tripTitle
        tripDate = args.tripDate

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }
        userId = currentUser.uid

        setupRecyclerView()
        setupUploadButton()
        setupNavigation()
        loadDocuments()
    }

    private fun loadDocuments() {
        firestore.collection("documents")
            .whereEqualTo("userId", userId)
            .whereEqualTo("tripId", tripId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Toast.makeText(requireContext(), "Error loading documents", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val list = mutableListOf<DocumentEntity>()

                snapshot?.documents?.forEach { doc ->
                    val document = doc.toObject(DocumentEntity::class.java)
                    document?.id = doc.id
                    document?.let { list.add(it) }
                }

                adapter.submitList(list)

                binding.tvEmpty.visibility =
                    if (list.isEmpty()) View.VISIBLE else View.GONE
            }
    }

    private fun deleteDocument(document: DocumentEntity) {
        firestore.collection("documents")
            .document(document.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setupRecyclerView() {
        adapter = DocumentAdapter { document ->
            deleteDocument(document)
        }
        binding.recyclerDocuments.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerDocuments.adapter = adapter
    }



    private fun setupUploadButton() {
        binding.btnUpload.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }
    }

    private fun handleFileSelected(uri: Uri) {

        val fileName = uri.lastPathSegment ?: "Unknown File"

        val document = hashMapOf(
            "userId" to userId,
            "tripId" to tripId,
            "fileName" to fileName,
            "fileUri" to uri.toString()
        )

        firestore.collection("documents")
            .add(document)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "File uploaded!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupNavigation() {
        binding.tabParticipants.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToTripDetailsFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        binding.tabPolls.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToCreatePollFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }


        binding.tabExpenses.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToExpenseSummaryFragment(
                        tripId,
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        binding.tabItinerary.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToItineraryFragment(
                        tripId,
                        tripTitle,
                        tripDate
            )

            findNavController().navigate(action)
        }

        binding.tabDocs.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "You're already on Docs",
                Toast.LENGTH_SHORT
            ).show()
        }

        val bottomNav: BottomNavigationView = binding.bottomNav

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}