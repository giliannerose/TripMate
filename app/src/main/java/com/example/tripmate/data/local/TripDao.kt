package com.example.tripmate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.lifecycle.LiveData
import androidx.room.OnConflictStrategy

import com.example.tripmate.data.model.TripEntity


@Dao
interface TripDao {

    //Create a new trip
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)


    //Get all trips
    @Query("SELECT * FROM trip_table WHERE userId = :userId ORDER BY id DESC")
    fun getTripsByUser(userId: Int): LiveData<List<TripEntity>>

    //Update a trip
    @Update
    suspend fun updateTrip(trip: TripEntity)

    //Delete a trip
    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("SELECT COUNT(*) FROM trip_table WHERE userId = :userId")
    fun getTripCount(userId: Int): LiveData<Int>

    @Query("""
SELECT COUNT(DISTINCT country)
FROM trip_table
WHERE country != '' AND userId = :userId
""")
    fun getCountriesVisited(userId: Int): LiveData<Int>

    @Query("SELECT * FROM trip_table WHERE id = :tripId")
    fun getTripById(tripId: Long): LiveData<TripEntity>
}