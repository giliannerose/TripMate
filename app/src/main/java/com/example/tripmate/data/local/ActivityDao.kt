package com.example.tripmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tripmate.data.model.ActivityEntity

@Dao
interface ActivityDao {

    @Insert
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("SELECT * FROM activity_table WHERE tripId = :tripId ORDER BY date ASC, time ASC")
    fun getActivitiesForTrip(tripId: Long): LiveData<List<ActivityEntity>>

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Query("DELETE FROM activity_table WHERE id = :id")
    suspend fun deleteActivity(id: Int)
}