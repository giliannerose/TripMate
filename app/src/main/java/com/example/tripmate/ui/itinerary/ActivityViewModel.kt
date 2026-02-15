package com.example.tripmate.ui.itinerary

import android.app.Application
import androidx.lifecycle.*
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.ActivityEntity
import com.example.tripmate.data.repository.ActivityRepository
import kotlinx.coroutines.launch

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ActivityRepository

    init {
        val dao = AppDatabase.getDatabase(application).activityDao()
        repository = ActivityRepository(dao)
    }

    fun getActivitiesForTrip(tripId: Long): LiveData<List<ActivityEntity>> {
        return repository.getActivitiesForTrip(tripId)
    }

    fun insert(activity: ActivityEntity) = viewModelScope.launch {
        repository.insert(activity)
    }

    fun update(activity: ActivityEntity) = viewModelScope.launch {
        repository.update(activity)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }
}