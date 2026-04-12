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
fun LoginScreen(
    authState: State<Unit>,
    onGoToRegister: () -> Unit,
    onLoginClick: (String, String) -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is State.Success) {
            localError = ""
            onLoginSuccess()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Student Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Log in with your student e-mail to manage your booth bookings.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            text = "Enter your student e-mail",
            style = MaterialTheme.typography.bodyMedium
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
            text = "Enter your password",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    email.isBlank() -> localError = "Please enter e-mail"
                    password.isBlank() -> localError = "Please enter password"
                    else -> {
                        localError = ""
                        onLoginClick(email.trim(), password.trim())
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onGoToRegister) {
            Text("Create account")
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