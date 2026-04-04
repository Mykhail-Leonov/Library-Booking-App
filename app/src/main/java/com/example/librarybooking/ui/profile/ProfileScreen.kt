package com.example.librarybooking.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class BookingItem(
    val boothName: String,
    val building: String,
    val date: String,
    val timeSlot: String
)

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditBooking: () -> Unit
) {
    val bookings = listOf(
        BookingItem("Booth 1", "David Hockney", "2026-04-07", "10:30 - 12:30"),
        BookingItem("Collaborative Space 1", "Library", "2026-04-09", "12:30 - 14:30")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Full Name: John Pork")
            Text("Student ID: 12345678")
            Text("Email: 12345678@bradfordcollege.ac.uk")

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "My Bookings",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(bookings) { booking ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(booking.boothName, style = MaterialTheme.typography.titleMedium)
                    Text("Building: ${booking.building}")
                    Text("Date: ${booking.date}")
                    Text("Time: ${booking.timeSlot}")

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onEditBooking,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Booking")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel Booking")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}