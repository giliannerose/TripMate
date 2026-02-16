package com.example.tripmate.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.TripParticipantEntity
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.repository.UserRepository
import com.example.tripmate.ui.trip.TripParticipantViewModel
import kotlinx.coroutines.launch

class UserViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val dao = AppDatabase
            .getDatabase(application)
            .userDao()

        repository = UserRepository(dao)
    }

    fun update(user: UserEntity) =
        viewModelScope.launch {
            repository.update(user)
        }

    fun insertAndLinkToTrip(
        user: UserEntity,
        tripId: Long,
        tripParticipantViewModel: TripParticipantViewModel
    ) = viewModelScope.launch {

        val userId = repository.insert(user)

        val participant = TripParticipantEntity(
            tripId = tripId,
            userId = userId.toInt()
        )

        tripParticipantViewModel.insert(participant)
    }
}