package com.example.myapplication.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.remote.User
import com.example.myapplication.repository.Repository
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    val repository: Repository
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getUsers()
                    .onSuccess {
                        _uiState.value = UiState.DisplayingUsers(it)
                    }
                    .onFailure {
                        _uiState.value = UiState.Error(it.message ?: "")
                    }

            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "")
            }
        }
    }

}


sealed class UiState {
    data object Loading: UiState()
    data class Error(val message: String): UiState()
    data class DisplayingUsers(
        val users: List<User>
    ): UiState()

}