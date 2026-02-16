package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "documents",
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
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: Int,

    val fileName: String,

    val fileUri: String,

    val uploadedAt: Long = System.currentTimeMillis()
)