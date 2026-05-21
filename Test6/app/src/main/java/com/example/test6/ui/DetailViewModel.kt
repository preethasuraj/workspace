package com.example.test6.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.test6.PropertyDetailsRoute
import com.example.test6.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
data class DetailViewModel @Inject constructor(
    val repository: PropertyRepository,
    val savedStateHandle: SavedStateHandle,
): ViewModel() {
    val route = savedStateHandle.toRoute<PropertyDetailsRoute>()

    var _uiState = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val uiState = _uiState.asStateFlow()
    init {
        fetchEmployee()
    }

    private fun fetchEmployee() {
        _uiState.value = DetailsState.Loading
        viewModelScope.launch {
            repository.getProperty(route.id)
                .onSuccess { _uiState.value = DetailsState.Success(it) }
        }
    }

}

sealed class DetailsState{
    data object Loading: DetailsState()
    data class Success(val entity: UiEntity): DetailsState()
}