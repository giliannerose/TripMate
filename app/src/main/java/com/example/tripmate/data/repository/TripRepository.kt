package com.example.tripmate.data.repository
import com.example.tripmate.data.local.TripDao
import com.example.tripmate.data.model.TripEntity
import androidx.lifecycle.LiveData

class TripRepository (private val tripDao: TripDao){

    // Get the LiveData stream. Notifies the UI whenever a trip is added or deleted
    val allTrips: LiveData<List<TripEntity>> = tripDao.getAllTrips()

    // Wrap the suspend functions to keep them 'suspend' so they can run on a background thread
    suspend fun insert(trip: TripEntity){
        tripDao.insertTrip(trip)

    }

    suspend fun delete(tripId: Long){
        tripDao.deleteTrip(tripId)
    }

    suspend fun update(trip: TripEntity){
        tripDao.updateTrip(trip)
    }

}
