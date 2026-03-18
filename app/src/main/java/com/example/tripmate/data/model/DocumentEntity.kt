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

    @PrimaryKey
    var id: String = "",

    var userId: String = "",

    var tripId: String = "",

    var fileName: String = "",

    var fileUri: String = "",

    var uploadedAt: Long = System.currentTimeMillis()
)