package com.example.librarybooking

sealed class State<out T> {
    object Idle : State<Nothing>()
    object Loading : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    data class Error(val message: String) : State<Nothing>()
}