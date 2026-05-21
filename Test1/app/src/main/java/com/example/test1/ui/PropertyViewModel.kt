package com.example.test1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
data class PropertyViewModel @Inject constructor(
    val repository: PropertyRepository,
) : ViewModel() {
    var _uiState = MutableStateFlow<PropertyUiState>(PropertyUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
         getProperties()

    }

    fun getProperties() {
        _uiState.value = PropertyUiState.Loading
        viewModelScope.launch {
            repository.getProperties()
                .onSuccess {
                    _uiState.value = PropertyUiState.Success(it)
                }
                .onFailure {
                    if (it.message == "Empty") {
                        _uiState.value = PropertyUiState.Empty
                    } else {
                        _uiState.value = PropertyUiState.Error(
                            it.message ?: "Error in fetching the properties"
                        )
                    }
                }
        }
    }
}