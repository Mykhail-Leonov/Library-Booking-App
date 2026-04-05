package com.example.librarybooking.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.librarybooking.State
import com.example.librarybooking.services.BookingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingView : ViewModel() {

    private val bookingService = BookingService()

    private val _bookingState = MutableStateFlow<State<Unit>>(State.Idle)
    val bookingState: StateFlow<State<Unit>> = _bookingState

    fun createBooking(
        boothName: String,
        date: String,
        timeSlot: String
    ) {
        viewModelScope.launch {
            _bookingState.value = State.Loading

            val result = bookingService.createBooking(
                boothName = boothName,
                date = date,
                timeSlot = timeSlot
            )

            _bookingState.value = result.fold(
                onSuccess = { State.Success(Unit) },
                onFailure = { State.Error(it.message ?: "Booking failed") }
            )
        }
    }

    fun resetState() {
        _bookingState.value = State.Idle
    }
}