package com.example.librarybooking.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.librarybooking.State

@Composable
fun RegisterScreen(
    authState: State<Unit>,
    onGoToLogin: () -> Unit,
    onRegisterClick: (String, String, String, String) -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is State.Success) {
            localError = ""
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create an Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Register in the system to book a booth",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                localError = ""
            },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = studentId,
            onValueChange = {
                studentId = it
                localError = ""
            },
            label = { Text("Student ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "e.g. 12345678",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                localError = ""
            },
            label = { Text("Student Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "e.g. 12345678@bradfordcollege.ac.uk",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                localError = ""
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Password must be at least 8 characters",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    fullName.isBlank() -> localError = "Name cannot be empty"
                    studentId.length != 8 || !studentId.all { it.isDigit() } ->
                        localError = "Student ID must be exactly 8 digits"
                    email.isBlank() -> localError = "Email cannot be empty"
                    !email.endsWith("@bradfordcollege.ac.uk") ->
                        localError = "Use your Bradford College email"
                    password.length < 8 ->
                        localError = "Password must be at least 8 characters"
                    else -> {
                        localError = ""
                        onRegisterClick(
                            fullName.trim(),
                            studentId.trim(),
                            email.trim(),
                            password.trim()
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onGoToLogin) {
            Text("Go to Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (authState) {
            is State.Loading -> CircularProgressIndicator()
            is State.Error -> Text(
                text = authState.message,
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