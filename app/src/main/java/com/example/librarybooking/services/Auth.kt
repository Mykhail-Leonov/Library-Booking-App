package com.example.librarybooking.services

import com.example.librarybooking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Auth {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        fullName: String,
        studentId: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("User ID not found"))

            val user = User(
                uid = uid,
                fullName = fullName,
                studentId = studentId,
                email = email,
                role = "student"
            )

            db.collection("users")
                .document(uid)
                .set(user)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUserProfile(): User? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = db.collection("users").document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }
}