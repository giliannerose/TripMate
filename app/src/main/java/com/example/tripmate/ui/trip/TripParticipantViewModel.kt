package com.example.tripmate.ui.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.TripParticipantEntity
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.repository.TripParticipantRepository
import kotlinx.coroutines.launch

class TripParticipantViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository: TripParticipantRepository

    init {
        val dao = AppDatabase
            .getDatabase(application)
            .tripParticipantDao()

        repository = TripParticipantRepository(dao)
    }

    fun getParticipants(tripId: Long) =
        repository.getParticipants(tripId)

    fun insert(entity: TripParticipantEntity) =
        viewModelScope.launch {
            repository.insert(entity)
        }

    fun remove(tripId: Long, userId: Int) =
        viewModelScope.launch {
            repository.remove(tripId, userId)
        }

    fun getBuddyCount(userId: Int): LiveData<Int> {
        return repository.getBuddyCount(userId)
    }

    fun getTripsJoinedCount(userId: Int): LiveData<Int> {
        return repository.getTripsJoinedCount(userId)
    }

}