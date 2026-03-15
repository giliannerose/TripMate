package com.example.tripmate.ui.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.data.repository.TripRepository
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TripRepository
    val allTrips: LiveData<List<TripEntity>>

    init {
        val tripDao = AppDatabase.getDatabase(application).tripDao()
        repository = TripRepository(tripDao)
        allTrips = repository.allTrips
    }

    fun insert(trip: TripEntity) = viewModelScope.launch {
        repository.insert(trip)
    }

    fun delete(trip: TripEntity) = viewModelScope.launch {
        repository.delete(trip)
    }

    fun update(trip: TripEntity) = viewModelScope.launch {
        repository.update(trip)
    }

    val tripCount: LiveData<Int> = repository.getTripCount()
    val countriesVisited: LiveData<Int> = repository.getCountriesVisited()

    fun getTripById(tripId: Long): LiveData<TripEntity> {
        return repository.getTripById(tripId)
    }
}