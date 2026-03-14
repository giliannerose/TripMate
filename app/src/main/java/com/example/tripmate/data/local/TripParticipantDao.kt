package com.example.tripmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tripmate.data.model.TripParticipantEntity
import com.example.tripmate.data.model.UserEntity

@Dao
interface TripParticipantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(participant: TripParticipantEntity)

    @Query("""
        SELECT user_table.*
        FROM user_table
        INNER JOIN trip_participant_table
        ON user_table.id = trip_participant_table.userId
        WHERE trip_participant_table.tripId = :tripId
    """)
    fun getParticipantsForTrip(tripId: Long): LiveData<List<UserEntity>>

    @Query("""
        DELETE FROM trip_participant_table
        WHERE tripId = :tripId AND userId = :userId
    """)
    suspend fun removeParticipant(tripId: Long, userId: Int)

    @Query("""
    SELECT COUNT(*) FROM trip_participant_table
    WHERE userId = :userId
""")
    fun getBuddyCount(userId: Int): LiveData<Int>

    @Query("""
    SELECT COUNT(DISTINCT tripId)
    FROM trip_participant_table
    WHERE userId = :userId
""")
    fun getTripsJoinedCount(userId: Int): LiveData<Int>
}