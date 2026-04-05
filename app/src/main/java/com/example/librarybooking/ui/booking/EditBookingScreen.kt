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
import com.example.librarybooking.State

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditBookingScreen(
    bookingId: String,
    boothName: String,
    currentDate: String,
    currentTimeSlot: String,
    onBack: () -> Unit
) {
    val editBookingView: EditBookingView = viewModel()
    val editState by editBookingView.editState.collectAsState()

    val dates = listOf(
        "2026-04-06",
        "2026-04-07",
        "2026-04-08",
        "2026-04-09",
        "2026-04-10"
    )

    val slots = listOf(
        "08:30 - 10:30",
        "10:30 - 12:30",
        "12:30 - 14:30",
        "14:30 - 16:30"
    )

    var selectedDate by remember { mutableStateOf(currentDate) }
    var selectedSlot by remember { mutableStateOf(currentTimeSlot) }

    LaunchedEffect(editState) {
        if (editState is State.Success) {
            editBookingView.resetState()
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Edit Booking",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = boothName,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose a new date and time slot.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Select New Date", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dates.forEach { date ->
                OutlinedButton(onClick = { selectedDate = date }) {
                    Text(date)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Select New Time Slot", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            slots.forEach { slot ->
                OutlinedButton(onClick = { selectedSlot = slot }) {
                    Text(slot)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedDate.isNotBlank() && selectedSlot.isNotBlank()) {
                    editBookingView.updateBooking(
                        bookingId = bookingId,
                        date = selectedDate,
                        timeSlot = selectedSlot
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (editState) {
            is State.Loading -> CircularProgressIndicator()
            is State.Error -> Text(
                text = (editState as State.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            else -> Unit
        }
    }
}