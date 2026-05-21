package com.example.test7.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test7.repository.HotelRepository
import com.example.test7.ui.list.UiEntity
import com.example.test7.ui.list.toUiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: HotelRepository,
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    val id = savedStateHandle.get<String>("id")

    init {
        getHotelDetails()
    }

    fun getHotelDetails() {
        viewModelScope.launch {
            try {
                repository.getHotelDetailsRemote(id)
                    .onSuccess {
                        _uiState.value = UiState.ShowingDetails(it.toUiEntity())
                    }
            } catch (e: Exception) {
                //_uiState.value = UiState.E
            }

        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class ShowingDetails(val hotel: UiEntity) : UiState()
}