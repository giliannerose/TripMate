package com.example.tripmate.data.repository

import com.example.tripmate.data.local.UserDao
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.utils.PasswordHasher


class UserRepository(private val userDao: UserDao) {
    suspend fun register(
        name: String,
        email: String,
        rawPassword: String
    ): Long? {

        if (userDao.getUserByEmail(email) != null) return null

        val hashedPassword = PasswordHasher.hash(rawPassword)

        val newUser = UserEntity(
            name = name,
            email = email,
            passwordHash = hashedPassword
        )

        return userDao.insert(newUser)
    }

    //Get the user and compare the hashes
    suspend fun login(email: String, inputPassword: String): UserEntity? {
        val user = userDao.getUserByEmail(email) ?: return null
        // Use check function from the helper
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

}