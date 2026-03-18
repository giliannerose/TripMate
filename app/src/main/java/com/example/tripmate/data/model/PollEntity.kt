package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "poll_table",
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
data class PollEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: String,

    val question: String,

    val option1: String,
    val option2: String,
    val option3: String?,
    val option4: String?,

    val createdAt: Long = System.currentTimeMillis()
)