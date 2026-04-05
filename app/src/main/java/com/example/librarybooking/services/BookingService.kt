package com.example.librarybooking.services

import com.example.librarybooking.models.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookingService {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun createBooking(
        boothName: String,
        date: String,
        timeSlot: String
    ): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            val userSnapshot = db.collection("users").document(uid).get().await()
            val userData = userSnapshot.data
                ?: return Result.failure(Exception("User profile not found"))

            val boothQuery = db.collection("booths")
                .whereEqualTo("name", boothName)
                .get()
                .await()

            val boothDocument = boothQuery.documents.firstOrNull()
                ?: return Result.failure(Exception("Booth not found"))

            val booking = Booking(
                userId = uid,
                userName = userData["fullName"] as? String ?: "",
                studentId = userData["studentId"] as? String ?: "",
                boothId = boothDocument.id,
                boothName = boothDocument.getString("name") ?: "",
                building = boothDocument.getString("building") ?: "",
                date = date,
                timeSlot = timeSlot,
                createdAt = System.currentTimeMillis()
            )

            db.collection("bookings")
                .add(booking)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUserBookings(): Result<List<Booking>> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            val snapshot = db.collection("bookings")
                .whereEqualTo("userId", uid)
                .get()
                .await()

            val bookings = snapshot.documents.mapNotNull { document ->
                document.toObject(Booking::class.java)?.copy(id = document.id)
            }.sortedBy { it.createdAt }

            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}