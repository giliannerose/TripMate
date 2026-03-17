package com.example.tripmate.data.repository

import androidx.lifecycle.LiveData
import com.example.tripmate.data.local.TripDao
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.data.remote.FirebaseStoreManager

class TripRepository(private val tripDao: TripDao) {

    private val firebaseStoreManager = FirebaseStoreManager()

    //val allTrips: LiveData<List<TripEntity>> = tripDao.getAllTrips()
    fun getTripsByUser(userId: String): LiveData<List<TripEntity>> {
        return tripDao.getTripsByUser(userId)
    }

    suspend fun insert(trip: TripEntity, userId: String? = null) {
        // 1. Insert into local Room database
        tripDao.insertTrip(trip)

        // 2. Sync to Firebase Cloud if userId is available
        userId?.let {
            firebaseStoreManager.saveTrip(trip, it) { success ->
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

    fun getTripCount(userId: String): LiveData<Int> {
        return tripDao.getTripCount(userId)
    }
    fun getCountriesVisited(userId: String): LiveData<Int> {
        return tripDao.getCountriesVisited(userId)
    }

    fun getTripById(tripId: Long): LiveData<TripEntity> {
        return tripDao.getTripById(tripId)
    }

    // Sync from Firebase → Room
    fun syncFromCloud(userId: String) {
        firebaseStoreManager.getTrips(userId) { trips ->
        }
    }
}