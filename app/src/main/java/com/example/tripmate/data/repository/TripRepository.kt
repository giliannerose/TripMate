package com.example.tripmate.data.repository
import com.example.tripmate.data.local.TripDao
import com.example.tripmate.data.model.TripEntity
import androidx.lifecycle.LiveData

class TripRepository(private val tripDao: TripDao) {

    fun getTripsByUser(userId: Int): LiveData<List<TripEntity>> {
        return tripDao.getTripsByUser(userId)
    }

    suspend fun insert(trip: TripEntity) {
        tripDao.insertTrip(trip)
    }

    suspend fun update(trip: TripEntity) {
        tripDao.updateTrip(trip)
    }

    suspend fun delete(trip: TripEntity) {
        tripDao.deleteTrip(trip)
    }

    fun getTripCount(userId: Int): LiveData<Int> {
        return tripDao.getTripCount(userId)
    }

    fun getCountriesVisited(userId: Int): LiveData<Int> {
        return tripDao.getCountriesVisited(userId)
    }

    fun getTripById(tripId: Long): LiveData<TripEntity> {
        return tripDao.getTripById(tripId)
    }

}
