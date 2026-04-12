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
                .whereEqualTo("name", boothName.trim())
                .get()
                .await()

            val boothDocument = boothQuery.documents.firstOrNull()
                ?: return Result.failure(Exception("Booth not found"))




            val existingUserBookings = db.collection("bookings")
                .whereEqualTo("userId", uid)
                .whereEqualTo("date", date)
                .get()
                .await()

            if (!existingUserBookings.isEmpty) {
                return Result.failure(Exception("You already have a booking on this date"))
            }


            val finalSlotCheck = db.collection("bookings")
                .whereEqualTo("boothId", boothDocument.id)
                .whereEqualTo("date", date)
                .whereEqualTo("timeSlot", timeSlot)
                .get().await()

            if (!finalSlotCheck.isEmpty) {
                return Result.failure(Exception("This slot was just taken by another student!"))
            }


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
            Result.failure(Exception("Booking failed. Please try again."))
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


    suspend fun cancelBooking(bookingId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Unauthorized"))


            val doc = db.collection("bookings").document(bookingId).get().await()


            if (doc.getString("userId") != uid) {
                return Result.failure(Exception("Security Error: You can only cancel your own bookings."))
            }

            doc.reference.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Cancellation failed. Please try again."))
        }
    }

    suspend fun updateBooking(
        bookingId: String,
        boothName: String,
        date: String,
        timeSlot: String
    ): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            val doc = db.collection("bookings").document(bookingId).get().await()


            if (doc.getString("userId") != uid) {
                return Result.failure(Exception("Unauthorized: You can only edit your own bookings."))
            }

            val boothQuery = db.collection("booths")
                .whereEqualTo("name", boothName.trim())
                .get()
                .await()

            val boothDocument = boothQuery.documents.firstOrNull()
                ?: return Result.failure(Exception("Booth not found"))

            val existingUserBookings = db.collection("bookings")
                .whereEqualTo("userId", uid)
                .whereEqualTo("date", date)
                .get()
                .await()

            val conflictingUserBooking = existingUserBookings.documents.any { it.id != bookingId }
            if (conflictingUserBooking) {
                return Result.failure(Exception("You already have a booking on this date"))
            }

            val existingSlotBookings = db.collection("bookings")
                .whereEqualTo("boothId", boothDocument.id)
                .whereEqualTo("date", date)
                .whereEqualTo("timeSlot", timeSlot)
                .get()
                .await()

            val conflictingSlotBooking = existingSlotBookings.documents.any { it.id != bookingId }
            if (conflictingSlotBooking) {
                return Result.failure(Exception("This slot is already booked"))
            }


            db.collection("bookings")
                .document(bookingId)


                .update(
                    mapOf(
                        "date" to date,
                        "timeSlot" to timeSlot
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookedSlots(
        boothName: String,
        date: String
    ): Result<List<String>> {
        return try {
            val boothQuery = db.collection("booths")
                .whereEqualTo("name", boothName.trim())
                .get()
                .await()

            val boothDocument = boothQuery.documents.firstOrNull()
                ?: return Result.failure(Exception("Booth not found"))

            val snapshot = db.collection("bookings")
                .whereEqualTo("boothId", boothDocument.id)
                .whereEqualTo("date", date)
                .get()
                .await()

            val bookedSlots = snapshot.documents.mapNotNull { it.getString("timeSlot") }


            Result.success(bookedSlots)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}