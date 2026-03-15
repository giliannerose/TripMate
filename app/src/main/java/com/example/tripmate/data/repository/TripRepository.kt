package com.example.tripmate.data.repository
import com.example.tripmate.data.local.TripDao
import com.example.tripmate.data.model.TripEntity
import androidx.lifecycle.LiveData

class TripRepository(private val tripDao: TripDao) {

    val allTrips: LiveData<List<TripEntity>> = tripDao.getAllTrips()

    suspend fun insert(trip: TripEntity) {
        tripDao.insertTrip(trip)
    }

    suspend fun update(trip: TripEntity) {
        tripDao.updateTrip(trip)
    }

    suspend fun delete(trip: TripEntity) {
        tripDao.deleteTrip(trip)
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

}
