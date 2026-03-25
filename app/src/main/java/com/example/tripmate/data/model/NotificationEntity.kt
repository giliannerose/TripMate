package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notification_table",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tripId")]
)
data class NotificationEntity(

    @PrimaryKey
    var id: String = "",

    val userId: String = "",

    val tripId: String = "",


    val tripTitle: String = "",
    val tripDate: String = "",

    val message: String = "",

    val type: String = "",
    // "INVITATION", "POLL", "EXPENSE", "INFO"

    val status: String = "PENDING",
    // "PENDING", "ACCEPTED", "DECLINED", "ACKNOWLEDGED"

    val createdAt: Long = System.currentTimeMillis()
)