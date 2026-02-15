package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_table")
data class ActivityEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: Long,   // Link to TripEntity

    val date: String,
    val time: String,
    val title: String,
    val notes: String
)