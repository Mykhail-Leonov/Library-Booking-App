package com.example.librarybooking.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.librarybooking.State
import com.example.librarybooking.models.User

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditBooking: () -> Unit
) {
    val profileView: ProfileView = viewModel()
    val userState by profileView.userState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

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

                Text("Name: ${user.fullName}")
                Spacer(modifier = Modifier.height(8.dp))

                Text("Student ID: ${user.studentId}")
                Spacer(modifier = Modifier.height(8.dp))

                Text("Email: ${user.email}")
                Spacer(modifier = Modifier.height(16.dp))
            }

            else -> Unit
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onEditBooking,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Edit Booking")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}