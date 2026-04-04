package com.example.librarybooking.services

import com.example.librarybooking.models.Booth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BoothService {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getBooths(): Result<List<Booth>> {
        return try {
            val snapshot = db.collection("booths").get().await()

            val booths = snapshot.documents.mapNotNull { document ->
                document.toObject(Booth::class.java)?.copy(id = document.id)
            }.sortedWith(compareBy { it.name })

            Result.success(booths)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}