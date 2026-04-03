package com.example.librarybooking

import android.app.Application
import com.google.firebase.FirebaseApp

class LibraryBookingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}