package com.example.tripmate.ui.poll

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.PollEntity
import com.example.tripmate.data.repository.PollRepository
import kotlinx.coroutines.launch

class PollViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PollRepository

    init {
        val dao = AppDatabase.getDatabase(application).pollDao()
        repository = PollRepository(dao)
    }

    fun getPollsByTrip(tripId: Long): LiveData<List<PollEntity>> {
        return repository.getPollsByTrip(tripId)
    }

    fun insertPoll(poll: PollEntity) {
        viewModelScope.launch {
            repository.insertPoll(poll)
        }
    }

    fun deletePoll(poll: PollEntity) {
        viewModelScope.launch {
            repository.deletePoll(poll)
        }
    }
}
