package com.example.tripmate.ui.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripmate.data.repository.DocumentRepository

class DocumentViewModelFactory(
    private val repository: DocumentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocumentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DocumentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}