package com.example.myapplication.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _searchText = MutableStateFlow<String>("")
    val uiState = combine(
        _uiState, _searchText, _searchText.debounce(500)
    ) { state, instant, text ->
        if (state is UiState.ShowingList) {
            state.copy(
                users = state.users.filter { it.name.contains(text, true) },
                searchText = instant
            )
        } else state

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            try {
                repository.getUsers()
                    .onSuccess { users ->
                        _uiState.value =
                            UiState.ShowingList(
                                users.map {
                                    UiUser(
                                        id = it.id,
                                        name = it.first + " ${it.last}",
                                    )
                                }
                            )

                    }
                    .onFailure {
                        _uiState.value = UiState.Error(it.message ?: "error")
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun updateSearch(query: String) {
        _searchText.debounce(500)
        _searchText.value = query
    }
}


data class UiUser(
    val id: Int,
    val name: String,
)

sealed class UiState {
    data object Loading : UiState()
    data class ShowingList(
        val users: List<UiUser>,
        val searchText: String = ""
    ) : UiState()

    data class Error(val message: String) : UiState()
}