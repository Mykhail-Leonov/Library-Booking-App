package com.example.librarybooking.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.librarybooking.State
import com.example.librarybooking.models.Booth
import com.example.librarybooking.services.BoothService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeView : ViewModel() {

    private val boothService = BoothService()

    private val _boothState = MutableStateFlow<State<List<Booth>>>(State.Loading)
    val boothState: StateFlow<State<List<Booth>>> = _boothState

    init {
        loadBooths()
    }


    fun loadBooths() {
        viewModelScope.launch {
            _boothState.value = State.Loading

            val result = boothService.getBooths()

            _boothState.value = result.fold(
                onSuccess = { State.Success(it) },
                onFailure = { State.Error(it.message ?: "Failed to load booths") }
            )
        }
    }
}