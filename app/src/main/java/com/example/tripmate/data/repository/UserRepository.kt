package com.example.tripmate.data.repository

import androidx.lifecycle.LiveData
import com.example.tripmate.data.local.UserDao
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.remote.FirebaseAuthManager
import com.example.tripmate.data.utils.PasswordHasher

class UserRepository(private val userDao: UserDao) {
    
    private val authManager = FirebaseAuthManager()

    suspend fun register(
        name: String,
        email: String,
        rawPassword: String
    ): Long? {
        // 1. Check if user exists locally
        if (userDao.getUserByEmail(email) != null) return null

        // 2. Hash password for local storage
        val hashedPassword = PasswordHasher.hash(rawPassword)

        val newUser = UserEntity(
            name = name,
            email = email,
            passwordHash = hashedPassword
        )

        // 3. Register in Firebase (Cloud)
        authManager.signUp(email, rawPassword) { success, message ->
            // You can handle cloud sync result here if needed
        }

        return userDao.insert(newUser)
    }

    suspend fun login(email: String, inputPassword: String): UserEntity? {
        // 1. Authenticate with Firebase (Cloud)
        authManager.login(email, inputPassword) { success, message ->
             // Cloud auth success/fail
        }

        // 2. Fallback/Primary check against Local DB
        val user = userDao.getUserByEmail(email) ?: return null
        val isPasswordCorrect = PasswordHasher.check(inputPassword, user.passwordHash)

        return if (isPasswordCorrect) user else null
    }

    suspend fun update(user: UserEntity) {
        userDao.update(user)
    }

    suspend fun insert(user: UserEntity): Long {
        return userDao.insert(user)
    }

    fun getAllUsers() = userDao.getAllUsers()

    fun getUserById(userId: Int) = userDao.getUserById(userId)

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    fun getUserByEmailLiveData(email: String): LiveData<UserEntity?> {
        return userDao.getUserByEmailLiveData(email)
    }
}
