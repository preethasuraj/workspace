package com.example.test3.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test3.repository.DataError
import com.example.test3.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
data class PropertyViewModel @Inject constructor(
    val repository: PropertyRepository
): ViewModel() {
    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getProperties()
    }

    private fun getProperties() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getProperties()
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    if(it == DataError.NetworkError) {
                        _uiState.value = UiState.Error
                    } else {
                        _uiState.value = UiState.Empty
                    }
                }
        }
    }

}