package com.example.tripmate.data.repository

import com.example.tripmate.data.local.NotificationDao
import com.example.tripmate.data.model.NotificationEntity

class NotificationRepository(private val dao: NotificationDao) {

    fun getNotifications(tripId: Long) =
        dao.getNotificationsForTrip(tripId)

    suspend fun insert(notification: NotificationEntity) =
        dao.insert(notification)

    suspend fun update(notification: NotificationEntity) =
        dao.update(notification)

    suspend fun delete(notification: NotificationEntity) =
        dao.delete(notification)
}