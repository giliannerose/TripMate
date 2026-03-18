package com.example.tripmate.ui.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.data.repository.TripRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TripRepository

    init {
        val tripDao = AppDatabase.getDatabase(application).tripDao()
        repository = TripRepository(tripDao)
    }

    fun getTripsByUser(userId: String): LiveData<List<TripEntity>> {
        return repository.getTripsByUser(userId)
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

    fun getTripCount(userId: String, onResult: (Int) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("trips")
            .whereEqualTo("ownerId", userId)
            .get()
            .addOnSuccessListener { result ->
                onResult(result.size())
            }
    }

    fun getCountriesVisited(userId: String, onResult: (Int) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("trips")
            .whereEqualTo("ownerId", userId)
            .get()
            .addOnSuccessListener { result ->

                val countries = result.mapNotNull { it.getString("country") }
                val uniqueCountries = countries.toSet()

                onResult(uniqueCountries.size)
            }
    }

    fun getTripById(tripId: Long): LiveData<TripEntity> {
        return repository.getTripById(tripId)
    }
}