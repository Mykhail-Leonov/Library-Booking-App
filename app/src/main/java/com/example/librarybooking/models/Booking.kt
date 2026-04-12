package com.example.librarybooking.models

data class Booking(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val studentId: String = "",
    val boothId: String = "",
    val boothName: String = "",
    val building: String = "",
    val date: String = "",
    val timeSlot: String = "",
    val createdAt: Long = 0L
)
