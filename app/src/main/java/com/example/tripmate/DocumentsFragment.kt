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

class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private var _binding: FragmentDocumentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DocumentViewModel
    private lateinit var adapter: DocumentAdapter

    private val args: DocumentsFragmentArgs by navArgs()
    private var tripId: Int = -1
    private var tripTitle: String = ""
    private var tripDate: String = ""

    // Modern file picker


    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { handleFileSelected(it, tripId) }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDocumentsBinding.bind(view)

       tripId = args.tripId.toInt()
        tripTitle = args.tripTitle
        tripDate = args.tripDate

        setupViewModel()
        setupRecyclerView()
        observeDocuments(tripId)
        setupUploadButton(tripId)
        setupNavigation()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = DocumentRepository(database.documentDao())
        val factory = DocumentViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)
            .get(DocumentViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = DocumentAdapter { document ->
            viewModel.delete(document)
        }

        binding.recyclerDocuments.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerDocuments.adapter = adapter
    }

    private fun observeDocuments(tripId: Int) {
        viewModel.getDocuments(tripId)
            .observe(viewLifecycleOwner) { documents ->
                adapter.submitList(documents)

                binding.tvEmpty.visibility =
                    if (documents.isEmpty()) View.VISIBLE else View.GONE
            }
    }

    private fun setupUploadButton(tripId: Int) {
        binding.btnUpload.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }
    }

    private fun handleFileSelected(uri: Uri, tripId: Int) {

        val fileName = uri.lastPathSegment ?: "Unknown File"

        val document = DocumentEntity(
            tripId = tripId,
            fileName = fileName,
            fileUri = uri.toString()
        )

        viewModel.insert(document)

        Toast.makeText(
            requireContext(),
            "File uploaded successfully!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupNavigation() {
        binding.tabParticipants.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToTripDetailsFragment(
                        tripId.toLong(),
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        binding.tabPolls.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToCreatePollFragment(
                        tripId.toLong(),
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }


        binding.tabExpenses.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToExpenseSummaryFragment(
                        tripId.toLong(),
                        tripTitle,
                        tripDate
                    )

            findNavController().navigate(action)
        }

        binding.tabItinerary.setOnClickListener {

            val action =
                DocumentsFragmentDirections
                    .actionDocumentsFragmentToItineraryFragment(
                        tripId.toLong(),
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