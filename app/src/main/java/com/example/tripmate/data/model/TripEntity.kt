package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_table")
data class TripEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val country: String = ""

)