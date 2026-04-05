package com.example.librarybooking.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.librarybooking.State
import com.example.librarybooking.services.BookingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditBookingView : ViewModel() {

    private val bookingService = BookingService()

    private val _editState = MutableStateFlow<State<Unit>>(State.Idle)
    val editState: StateFlow<State<Unit>> = _editState

    fun updateBooking(
        bookingId: String,
        date: String,
        timeSlot: String
    ) {
        viewModelScope.launch {
            _editState.value = State.Loading

            val result = bookingService.updateBooking(
                bookingId = bookingId,
                date = date,
                timeSlot = timeSlot
            )

            _editState.value = result.fold(
                onSuccess = { State.Success(Unit) },
                onFailure = { State.Error(it.message ?: "Failed to update booking") }
            )
        }
    }

    fun resetState() {
        _editState.value = State.Idle
    }
}