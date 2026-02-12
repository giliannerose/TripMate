package com.example.tripmate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tripmate.data.model.UserEntity


@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM user_table WHERE name = :name LIMIT 1")
    suspend fun getUserByName(name: String): UserEntity?


}
