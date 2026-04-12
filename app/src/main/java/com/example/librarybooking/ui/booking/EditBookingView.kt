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

    private val _bookedSlotsState = MutableStateFlow<State<List<String>>>(State.Idle)
    val bookedSlotsState: StateFlow<State<List<String>>> = _bookedSlotsState


    fun updateBooking(
        bookingId: String,
        boothName: String,
        date: String,
        timeSlot: String
    ) {
        viewModelScope.launch {
            _editState.value = State.Loading

            val result = bookingService.updateBooking(
                bookingId = bookingId,
                boothName = boothName,
                date = date,
                timeSlot = timeSlot
            )

            _editState.value = result.fold(
                onSuccess = { State.Success(Unit) },
                onFailure = { State.Error(it.message ?: "Failed to update booking") }
            )
        }
    }
    fun loadBookedSlots(
        boothName: String,
        date: String
    ) {
        viewModelScope.launch {
            _bookedSlotsState.value = State.Loading

            val result = bookingService.getBookedSlots(boothName, date)

            _bookedSlotsState.value = result.fold(
                onSuccess = { State.Success(it) },
                onFailure = { State.Error(it.message ?: "Failed to load booked slots") }
            )
        }
    }

    fun resetState() {
        _editState.value = State.Idle
    }
}