package com.example.tripmate.ui.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.model.DocumentEntity
import com.example.tripmate.data.repository.DocumentRepository
import kotlinx.coroutines.launch

class DocumentViewModel(
    private val repository: DocumentRepository
) : ViewModel() {

    fun getDocuments(tripId: Int) =
        repository.getDocuments(tripId)

    fun insert(document: DocumentEntity) =
        viewModelScope.launch {
            repository.insert(document)
        }

    fun delete(document: DocumentEntity) =
        viewModelScope.launch {
            repository.delete(document)
        }
}