package com.example.tripmate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.lifecycle.LiveData

import com.example.tripmate.data.model.TripEntity


@Dao
interface TripDao {

    //Create a new trip
    @Insert
    suspend fun insertTrip(trip: TripEntity)


    //Get all trips
    @Query("SELECT * FROM trip_table ORDER BY date DESC")
    fun getAllTrips(): LiveData<List<TripEntity>>

    //Update a trip
    @Update
    suspend fun updateTrip(trip: TripEntity)

    //Delete a trip
    @Query("DELETE FROM trip_table WHERE id = :id")
    suspend fun deleteTrip(id: Long)
}