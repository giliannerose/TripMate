package com.example.tripmate.data.model

import androidx.room.Entity

@Entity(
    tableName = "trip_participant_table",
    primaryKeys = ["tripId", "userId"]
)
data class TripParticipantEntity(
    val tripId: Long,
    val userId: Int
)