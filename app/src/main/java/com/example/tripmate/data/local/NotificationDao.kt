package com.example.tripmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tripmate.data.model.NotificationEntity

@Dao
interface NotificationDao {

    @Insert
    suspend fun insert(notification: NotificationEntity)

    @Update
    suspend fun update(notification: NotificationEntity)

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("SELECT * FROM notification_table WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getNotificationsForTrip(tripId: Long): LiveData<List<NotificationEntity>>
}