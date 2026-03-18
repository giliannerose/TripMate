package com.example.tripmate.data.repository

import androidx.lifecycle.LiveData
import com.example.tripmate.data.local.TripParticipantDao
import com.example.tripmate.data.model.TripParticipantEntity

class TripParticipantRepository(
    private val dao: TripParticipantDao
) {

    fun getParticipants(tripId: Long) =
        dao.getParticipantsForTrip(tripId)

    suspend fun insert(entity: TripParticipantEntity) =
        dao.insert(entity)

    suspend fun remove(tripId: Long, userId: String) =
        dao.removeParticipant(tripId, userId)

    fun getBuddyCount(userId: String): LiveData<Int> {
        return dao.getBuddyCount(userId)
    }

    fun getTripsJoinedCount(userId: String): LiveData<Int> {
        return dao.getTripsJoinedCount(userId)
    }
}