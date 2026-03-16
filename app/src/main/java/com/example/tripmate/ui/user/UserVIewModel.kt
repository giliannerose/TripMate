package com.example.tripmate.ui.user

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.remote.FirebaseAuthManager
import com.example.tripmate.data.remote.FirebaseStorageManager
import com.example.tripmate.data.remote.FirebaseStoreManager
import com.example.tripmate.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val authManager = FirebaseAuthManager()
    private val storageManager = FirebaseStorageManager()
    private val storeManager = FirebaseStoreManager()

    // Registration result (Local Room DB)
    private val _registerResult = MutableLiveData<Long?>()
    val registerResult: LiveData<Long?> = _registerResult

    // Login Result for Firebase
    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>> = _loginResult

    init {
        val dao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(dao)
    }

    // --- Firebase Login Logic ---
    fun login(email: String, password: String) {
        authManager.login(email, password) { success, message ->
            _loginResult.postValue(Pair(success, message))
        }
    }

    // --- Local Database Methods (Room) ---
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = repository.register(name, email, password)
            _registerResult.postValue(result)
        }
    }

    fun update(user: UserEntity) = viewModelScope.launch {
        repository.update(user)
    }

    fun getUserById(userId: Int) = repository.getUserById(userId)

    fun getUserByEmailLiveData(email: String) = repository.getUserByEmailLiveData(email)

    suspend fun getUserByEmail(email: String) = repository.getUserByEmail(email)

    val allUsers = repository.getAllUsers()

    fun checkEmailExists(email: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            callback(user != null)
        }
    }

    fun insert(user: UserEntity, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insert(user)
            onResult(id)
        }
    }

    fun syncUserToCloud(user: UserEntity) {
        storeManager.saveUser(user) { success ->
            if (success) {

            }
        }
    }

    fun getCurrentUserEmail(): String {
        return authManager.getCurrentUser()?.email ?: "No Email"
    }

    fun saveProfileWithImage(userId: String, user: UserEntity, imageUri: Uri?) {
        if (imageUri != null) {
            // Upload the image to Firebase Storage first
            storageManager.uploadProfileImage(userId, imageUri) { downloadUrl ->
                if (downloadUrl != null) {
                    // If upload is successful, update the user object with the real URL
                    val updatedUser = user.copy(profileImageUri = downloadUrl)
                    // save the updated user to firestore
                    storeManager.saveUser(updatedUser) { success ->
                        if (success) {
                            android.util.Log.d("Firebase", "Profile and Image synced!")
                        }
                    }
                }
            }
        } else {
            // If no image, just save the text data to Firestore
            storeManager.saveUser(user) { /* handle result */ }
        }
    }
}