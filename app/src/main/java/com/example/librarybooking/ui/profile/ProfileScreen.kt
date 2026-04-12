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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.librarybooking.Notifications
import com.example.librarybooking.State
import com.example.librarybooking.models.Booking
import com.example.librarybooking.models.User

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditBooking: (String, String, String, String) -> Unit
) {
    val context = LocalContext.current
    val profileView: ProfileView = viewModel()
    val userState by profileView.userState.collectAsState()

    val profileBookingView: ProfileBookingView = viewModel()
    val bookingState by profileBookingView.bookingState.collectAsState()
    val cancelState by profileBookingView.cancelState.collectAsState()

    LaunchedEffect(cancelState) {
        if (cancelState is State.Success) {
            Notifications.show(
                context = context,
                title = "Booking Cancelled",
                message = "Your booking was successfully cancelled"
            )
            profileBookingView.resetCancelState()
        }
    }


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

            when (userState) {
                is State.Loading -> {
                    CircularProgressIndicator()
                }
                is State.Error -> {
                    Text(
                        text = (userState as State.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is State.Success -> {
                    val user = (userState as State.Success<User>).data
                    Text("Full Name: ${user.fullName}")
                    Text("Student ID: ${user.studentId}")
                    Text("Email: ${user.email}")
                }
                else -> Unit
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "My Bookings",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        when (bookingState) {
            is State.Loading -> {
                item {
                    CircularProgressIndicator()
                }
            }
            is State.Error -> {
                item {
                    Text(
                        text = (bookingState as State.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is State.Success -> {
                val bookings = (bookingState as State.Success<List<Booking>>).data

                if (bookings.isEmpty()) {
                    item {
                        Text("No bookings yet")
                    }
                } else {
                    items(bookings) { booking ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    booking.boothName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Building: ${booking.building}")
                                Text("Date: ${booking.date}")
                                Text("Time: ${booking.timeSlot}")

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        onEditBooking(
                                            booking.id,
                                            booking.boothName,
                                            booking.date,
                                            booking.timeSlot
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Edit Booking")
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        profileBookingView.cancelBooking(booking.id)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Cancel Booking")
                                }
                            }
                        }
                    }
                }
            }
            else -> Unit
        }

        item {
            when (cancelState) {
                is State.Loading -> {
                    CircularProgressIndicator()
                }
                is State.Error -> {
                    Text(
                        text = (cancelState as State.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> Unit
            }

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