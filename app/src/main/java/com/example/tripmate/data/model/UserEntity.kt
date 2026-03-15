package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity (

    @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val email: String,
        val passwordHash: String,

        val bio: String = "",
        val profileImageUri: String? = null,

    val gender: String = "",
    val age: Int = 0,
    val region: String = "",

    val notificationsEnabled: Boolean = true,
    val privacyStatus: String = "Public",

    val createdAt: Long = System.currentTimeMillis()

)

