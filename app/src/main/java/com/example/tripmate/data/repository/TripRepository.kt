package com.example.tripmate.data.repository

import androidx.lifecycle.LiveData
import com.example.tripmate.data.local.TripDao
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.data.remote.FirebaseStoreManager

class TripRepository(private val tripDao: TripDao) {

    private val firebaseStoreManager = FirebaseStoreManager()

    val allTrips: LiveData<List<TripEntity>> = tripDao.getAllTrips()

    suspend fun insert(trip: TripEntity, userId: String? = null) {
        // 1. Insert into local Room database
        tripDao.insertTrip(trip)

        // 2. Sync to Firebase Cloud if userId is available
        userId?.let {
            firebaseStoreManager.saveTrip(trip, it) { success ->
                // Handle success/failure of cloud sync if needed
            }
        }
    }

    suspend fun update(trip: TripEntity) {
        tripDao.updateTrip(trip)
        // Optionally sync update to Firebase
    }

    suspend fun delete(trip: TripEntity) {
        tripDao.deleteTrip(trip)
        // Optionally sync delete to Firebase
    }

    fun getTripCount(): LiveData<Int> {
        return tripDao.getTripCount()
    }

    fun getCountriesVisited(): LiveData<Int> {
        return tripDao.getCountriesVisited()
    }

    fun getTripById(tripId: Long): LiveData<TripEntity> {
        return tripDao.getTripById(tripId)
    }

    // Sync from Cloud to Local
    fun syncFromCloud(userId: String) {
        firebaseStoreManager.getTrips(userId) { trips ->
            // You can implement logic to update local DB with cloud data
        }
    }
}
