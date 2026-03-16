package com.example.tripmate.data.remote

import com.example.tripmate.data.model.TripEntity
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseStoreManager {
    private val db = FirebaseFirestore.getInstance()
    private val tripsCollection = db.collection("trips")

    // Save a trip to the cloud
    fun saveTrip(trip: TripEntity, userId: String, onResult: (Boolean) -> Unit) {
        val tripData = hashMapOf(
            "name" to trip.name,
            "description" to trip.description,
            "date" to trip.date,
            "ownerId" to userId
        )

        tripsCollection.add(tripData)
            .addOnCompleteListener { task ->

                onResult(task.isSuccessful)
            }
    }

    fun saveUser(user: com.example.tripmate.data.model.UserEntity, onResult: (Boolean) -> Unit) {
        db.collection("users")
            .document(user.email)
            .set(user)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

    // Get all trips for the logged-in user
    fun getTrips(userId: String, onResult: (List<TripEntity>) -> Unit) {
        tripsCollection.whereEqualTo("ownerId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val tripList = documents.map { doc ->
                    TripEntity(
                        id = 0,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        date = doc.getString("date") ?: ""
                    )
                }
                onResult(tripList)
            }
    }
}