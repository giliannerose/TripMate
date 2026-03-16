package com.example.tripmate.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FirebaseStorageManager {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    // Upload an image (like a profile picture)
    fun uploadImage(imageUri: Uri, folder: String, onResult: (Boolean, String?) -> Unit) {
        val fileName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("$folder/$fileName")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    onResult(true, uri.toString())
                }
            }
            .addOnFailureListener {
                onResult(false, it.message)
            }
    }

    // Upload a document
    fun uploadDocument(fileUri: Uri, folder: String, onResult: (Boolean, String?) -> Unit) {
        val fileName = UUID.randomUUID().toString()
        val docRef = storageRef.child("$folder/$fileName")

        docRef.putFile(fileUri)
            .addOnSuccessListener {
                docRef.downloadUrl.addOnSuccessListener { uri ->
                    onResult(true, uri.toString())
                }
            }
            .addOnFailureListener {
                onResult(false, it.message)
            }
    }
}
