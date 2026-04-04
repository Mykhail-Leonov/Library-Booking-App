package com.example.librarybooking.models

data class User(
    val uid: String = "",
    val fullName: String = "",
    val studentId: String = "",
    val email: String = "",
    val role: String = "student"
)