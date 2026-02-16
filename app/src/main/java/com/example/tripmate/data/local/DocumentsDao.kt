package com.example.tripmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tripmate.data.model.DocumentEntity

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: DocumentEntity)

    @Delete
    suspend fun delete(document: DocumentEntity)

    @Query("SELECT * FROM documents WHERE tripId = :tripId ORDER BY uploadedAt DESC")
    fun getDocumentsForTrip(tripId: Int): LiveData<List<DocumentEntity>>
}