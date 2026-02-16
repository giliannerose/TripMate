package com.example.tripmate.data.repository

import com.example.tripmate.data.local.DocumentDao
import com.example.tripmate.data.model.DocumentEntity

class DocumentRepository(private val dao: DocumentDao) {

    fun getDocuments(tripId: Int) = dao.getDocumentsForTrip(tripId)

    suspend fun insert(document: DocumentEntity) {
        dao.insert(document)
    }

    suspend fun delete(document: DocumentEntity) {
        dao.delete(document)
    }
}