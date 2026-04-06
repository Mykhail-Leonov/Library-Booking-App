package com.example.librarybooking.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.librarybooking.BookingDate
import com.example.librarybooking.State

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingScreen(
    boothName: String,
    onBack: () -> Unit
) {
    val bookingView: BookingView = viewModel()
    val bookingState by bookingView.bookingState.collectAsState()
    val bookedSlotsState by bookingView.bookedSlotsState.collectAsState()

    val dates = BookingDate.getAvailableDates()

    val slots = listOf(
        "08:30 - 10:30",
        "10:30 - 12:30",
        "12:30 - 14:30",
        "14:30 - 16:30"
    )

    var selectedDate by remember { mutableStateOf("") }
    var selectedSlot by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    LaunchedEffect(bookingState) {
        if (bookingState is State.Success) {
            bookingView.resetState()
            onBack()
        }
    }

    LaunchedEffect(selectedDate) {
        if (selectedDate.isNotBlank()) {
            bookingView.loadBookedSlots(boothName, selectedDate)
        }
    }

    val bookedSlots: List<String> = when (val state = bookedSlotsState) {
        is State.Success<List<String>> -> state.data
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = boothName,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose a date and time slot for your booking.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Select Date", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dates.forEach { date ->
                val displayDate = BookingDate.toDisplay(date)
                val storageDate = BookingDate.toStorage(date)

                OutlinedButton(
                    onClick = {
                        selectedDate = storageDate
                        selectedSlot = ""
                        localError = ""
                    }
                ) {
                    Text(displayDate)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Select Time Slot", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            slots.forEach { slot ->
                OutlinedButton(
                    onClick = {
                        selectedSlot = slot
                        localError = ""
                    },
                    enabled = slot !in bookedSlots
                ) {
                    Text(slot)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    selectedDate.isBlank() -> localError = "Please select a date"
                    selectedSlot.isBlank() -> localError = "Please select a time slot"
                    else -> {
                        localError = ""
                        bookingView.createBooking(
                            boothName = boothName,
                            date = selectedDate,
                            timeSlot = selectedSlot
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Booking")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = bookingState) {
            is State.Loading -> CircularProgressIndicator()
            is State.Error -> Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error
            )
            else -> Unit
        }

        if (localError.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = localError,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}