package com.example.librarybooking.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.librarybooking.State
import com.example.librarybooking.models.Booking
import com.example.librarybooking.services.BookingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileBookingView : ViewModel() {

    private val bookingService = BookingService()

    private val _bookingState = MutableStateFlow<State<List<Booking>>>(State.Loading)
    val bookingState: StateFlow<State<List<Booking>>> = _bookingState

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _bookingState.value = State.Loading

            val result = bookingService.getCurrentUserBookings()

            _bookingState.value = result.fold(
                onSuccess = { State.Success(it) },
                onFailure = { State.Error(it.message ?: "Failed to load bookings") }
            )
        }
    }
}