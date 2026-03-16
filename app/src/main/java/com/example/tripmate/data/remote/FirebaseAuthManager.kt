package com.example.tripmate.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Create a new account
    fun signUp(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, task.exception?.message)
            }
    }

    // Login existing account
    fun login(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful, task.exception?.message)
            }
    }

    fun logout() = auth.signOut()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}