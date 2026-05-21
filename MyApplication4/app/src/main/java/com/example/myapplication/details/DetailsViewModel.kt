package com.example.myapplication.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DetailsViewModel(
    val savedStateHandle: SavedStateHandle,
) :
    ViewModel() {

    val  uiState: UserDetails
    init {
        val id = savedStateHandle.get<String>("id")
        uiState = getUser()
    }

    fun getUser() = UserDetails(
        name = "Name"
    )
}

data class UserDetails(
    val name: String
)