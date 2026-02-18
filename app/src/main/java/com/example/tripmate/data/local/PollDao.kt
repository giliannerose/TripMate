package com.example.tripmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tripmate.data.model.PollEntity

@Dao
interface PollDao {

    @Insert
    suspend fun insert(poll: PollEntity): Long

    @Update
    suspend fun update(poll: PollEntity)

    @Delete
    suspend fun delete(poll: PollEntity)

    @Query("SELECT * FROM poll_table WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getPollsByTrip(tripId: Long): LiveData<List<PollEntity>>
}