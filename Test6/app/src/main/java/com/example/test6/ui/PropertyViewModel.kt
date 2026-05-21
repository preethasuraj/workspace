package com.example.test6.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test6.network.PropertyService
import com.example.test6.network.toUi
import com.example.test6.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    val propertyRepository: PropertyRepository,
) : ViewModel() {
    var _error = MutableStateFlow<String?>(null)
    var _refreshing = MutableStateFlow<Boolean>(false)
    val refreshing = _refreshing.asStateFlow()
    val uiState = combine(
        propertyRepository.properties, _error
    ) { properties, error ->
        when{
            properties.isEmpty() && error == null -> UiState.Empty
            error != null -> UiState.Error
            else -> UiState.Success(properties)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        fetchProperties()
    }

     fun fetchProperties() {
        if (_refreshing.value) {
            return
        }
        viewModelScope.launch {
            _refreshing.value = true
            propertyRepository.getProperties()
                .onFailure {
                    _error.value = it.message
                    _refreshing.value = false
                }
                .onSuccess {
                    _error.value = null
                    _refreshing.value = false
                }
        }
    }

}


sealed class UiState {
    data object Loading : UiState()
    data object Error : UiState()
    data object Empty : UiState()
    data class Success(val properties: List<UiEntity>) : UiState()
}