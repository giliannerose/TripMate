package com.example.tripmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tripmate.data.model.UserEntity


@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM user_table WHERE name = :name LIMIT 1")
    suspend fun getUserByName(name: String): UserEntity?

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Int): LiveData<UserEntity?>



}
