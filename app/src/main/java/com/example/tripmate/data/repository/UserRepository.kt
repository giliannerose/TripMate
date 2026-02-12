package com.example.tripmate.data.repository

import com.example.tripmate.data.local.UserDao
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.data.utils.PasswordHasher


class UserRepository(private val userDao: UserDao) {
    suspend fun register(name: String, email:String,rawPassword: String): Boolean{
       // checks if email already exist
        if (userDao.getUserByEmail(email) != null) return false

        val hashedPassword = PasswordHasher.hash(rawPassword)
        val newUser = UserEntity(name = name, email = email, passwordHash = hashedPassword)

        userDao.insertUser(newUser)
        return true
    }

    //Get the user and compare the hashes
    suspend fun login(email: String, inputPassword: String): UserEntity? {
        val user = userDao.getUserByEmail(email) ?: return null
        // Use check function from the helper
        val isPasswordCorrect = PasswordHasher.check(inputPassword, user.passwordHash)

        return if (isPasswordCorrect) user else null
    }

}