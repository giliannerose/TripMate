package com.example.tripmate.data.repository

import androidx.lifecycle.LiveData
import com.example.tripmate.data.local.PollDao
import com.example.tripmate.data.model.PollEntity

class PollRepository(private val pollDao: PollDao) {

    fun getPollsByTrip(tripId: Long): LiveData<List<PollEntity>> {
        return pollDao.getPollsByTrip(tripId)
    }

    suspend fun insertPoll(poll: PollEntity) {
        pollDao.insert(poll)
    }

    suspend fun updatePoll(poll: PollEntity) {
        pollDao.update(poll)
    }

    suspend fun deletePoll(poll: PollEntity) {
        pollDao.delete(poll)
    }
}
