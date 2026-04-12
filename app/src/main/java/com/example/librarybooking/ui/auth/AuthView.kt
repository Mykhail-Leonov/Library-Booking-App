package com.example.librarybooking.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.librarybooking.State
import com.example.librarybooking.services.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthView : ViewModel() {

    private val auth = Auth()

    private val _authState = MutableStateFlow<State<Unit>>(State.Idle)
    val authState: StateFlow<State<Unit>> = _authState

    fun isLoggedIn(): Boolean {
        return auth.getCurrentUserId() != null
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = State.Loading

            val result = auth.login(email, password)

            _authState.value = result.fold(
                onSuccess = { State.Success(Unit) },
                onFailure = { State.Error(it.message ?: "Login failed") }
            )
        }
    }


    fun register(
        fullName: String,
        studentId: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _authState.value = State.Loading

            val result = auth.register(
                fullName = fullName,
                studentId = studentId,
                email = email,
                password = password
            )

            _authState.value = result.fold(
                onSuccess = { State.Success(Unit) },
                onFailure = { State.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun logout() {
        auth.logout()
        _authState.value = State.Idle
    }


    fun resetState() {
        _authState.value = State.Idle
    }
}