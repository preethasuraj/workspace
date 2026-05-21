package com.example.test5.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test5.network.repository.NetworkError
import com.example.test5.network.repository.PropertyRepository
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

    fun getProperties() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getProperties()
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    if(it == NetworkError.Error) {
                        _uiState.value = UiState.Error
                    } else {
                        _uiState.value = UiState.Empty
                    }
                }
        }

    }
}




sealed class UiState{
    data object Loading: UiState()
    data object Error: UiState()
    data object Empty: UiState()
    data class Success(val properties: List<PropertyUiEntity>): UiState()
}