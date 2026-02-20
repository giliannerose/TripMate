package com.example.tripmate.ui.notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.NotificationEntity
import com.example.tripmate.data.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotificationRepository

    init {
        val dao = AppDatabase.getDatabase(application).notificationDao()
        repository = NotificationRepository(dao)
    }

    fun getNotifications(tripId: Long) =
        repository.getNotifications(tripId)

    fun update(notification: NotificationEntity) =
        viewModelScope.launch {
            repository.update(notification)
        }
}