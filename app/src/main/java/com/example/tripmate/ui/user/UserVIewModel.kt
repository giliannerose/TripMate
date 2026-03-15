package com.example.tripmate.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _registerResult = MutableLiveData<Long?>()
    val registerResult: LiveData<Long?> = _registerResult


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

    fun insert(user: UserEntity, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insert(user)
            onResult(id)
        }
    }

    fun getUserById(userId: Int) =
        repository.getUserById(userId)
    val allUsers = repository.getAllUsers()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = repository.register(name, email, password)
            _registerResult.postValue(result)
        }
    }


}