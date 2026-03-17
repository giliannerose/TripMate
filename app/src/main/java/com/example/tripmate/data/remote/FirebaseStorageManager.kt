package com.example.tripmate.data.remote

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FirebaseStorageManager {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    /**
     * Uploads any file type (Image or Doc) and returns the Download URL.
     */
    fun uploadFile(fileUri: Uri, folder: String, context: Context, onResult: (Boolean, String?) -> Unit) {
        // Automatically detect file extension (e.g., .jpg, .pdf, .docx)
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(fileUri)) ?: "file"

        val fileName = "${UUID.randomUUID()}.$extension"
        val fileRef = storageRef.child("$folder/$fileName")

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    onResult(true, uri.toString())
                }
            }
            .addOnFailureListener {
                onResult(false, it.message)
            }
    }
}