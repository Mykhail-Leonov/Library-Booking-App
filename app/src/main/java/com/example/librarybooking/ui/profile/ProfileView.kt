package com.example.librarybooking.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.librarybooking.State
import com.example.librarybooking.models.User
import com.example.librarybooking.services.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileView : ViewModel() {

    private val auth = Auth()
    private val _userState = MutableStateFlow<State<User>>(State.Loading)
    val userState: StateFlow<State<User>> = _userState

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _userState.value = State.Loading

            try {
                val user = auth.getCurrentUserProfile()
                if (user != null) {
                    _userState.value = State.Success(user)
                } else {
                    _userState.value = State.Error("User not found")
                }
            } catch (e: Exception) {
                _userState.value = State.Error(e.message ?: "Error loading profile")
            }
        }
    }
}