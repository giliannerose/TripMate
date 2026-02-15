package com.example.tripmate.data.repository

import androidx.lifecycle.LiveData
import com.example.tripmate.data.local.ActivityDao
import com.example.tripmate.data.model.ActivityEntity

class ActivityRepository(private val activityDao: ActivityDao) {

    fun getActivitiesForTrip(tripId: Long): LiveData<List<ActivityEntity>> {
        return activityDao.getActivitiesForTrip(tripId)
    }

    suspend fun insert(activity: ActivityEntity) {
        activityDao.insertActivity(activity)
    }

    suspend fun update(activity: ActivityEntity) {
        activityDao.updateActivity(activity)
    }

    suspend fun delete(id: Int) {
        activityDao.deleteActivity(id)
    }
}