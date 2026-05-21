package com.example.test4.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test4.repository.NetworkError
import com.example.test4.repository.PropertyRepository
import com.example.test4.ui.UiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
data class PropertyViewModel @Inject constructor(
    val repository: PropertyRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow<UiState>(Loading)
    private var _searchText = MutableStateFlow<String>("")
    val searchText = _searchText.asStateFlow()

    val uiState = combine(_uiState, _searchText) { state, text ->
        when (state) {
            is Success -> UiState.Success(state.properties.filter {
                it.name.contains(text, ignoreCase = true)
            })

            else -> state
        }
    }.stateIn<UiState>(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        getProperties()
    }

    private fun getProperties() {
        _uiState.value = Loading
        viewModelScope.launch {
            repository.getProperties()
                .onSuccess {
                    _uiState.value = Success(it)
                }
                .onFailure {
                    if (it is NetworkError.EmptyResponse) {
                        _uiState.value = Empty
                    } else {
                        _uiState.value = Error
                    }
                }

        }

    }
    fun updateText(text: String) {
        _searchText.value = text
    }

}