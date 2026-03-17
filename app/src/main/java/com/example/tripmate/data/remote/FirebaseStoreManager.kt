package com.example.tripmate.data.remote

import android.util.Log
import com.example.tripmate.data.model.TripEntity
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseStoreManager {
    private val db = FirebaseFirestore.getInstance()
    private val tripsCollection = db.collection("trips")

    // Save a trip to the cloud
    fun saveTrip(trip: TripEntity, userId: String, onResult: (Boolean) -> Unit) {
        // 1. Prepare the data
        val tripData = hashMapOf(
            "name" to trip.name,
            "description" to trip.description,
            "date" to trip.date,
            "ownerId" to userId
        )

        Log.d("TRIPMATE_DEBUG", "Attempting to save trip: ${trip.name}")

        // 2. Save to Firestore
        tripsCollection.add(tripData)
            .addOnSuccessListener { documentReference ->
                Log.d("TRIPMATE_DEBUG", "Trip saved successfully with ID: ${documentReference.id}")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("TRIPMATE_DEBUG", "Error saving trip", e)
                onResult(false)
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