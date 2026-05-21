package com.example.hotels4.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotels4.repository.HotelRepository
import com.example.hotels4.ui.list.UiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: HotelRepository,
    val savedStateHandle: SavedStateHandle,
): ViewModel(){
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    val id = savedStateHandle.get<String>("id")

    init {
        getDetails()
    }

    fun getDetails() {
        viewModelScope.launch {
            try {
                repository.getHotels()
                    .onSuccess { domainEntities ->
                        val hotel = domainEntities.first { it.id == id }
                        _uiState.value = UiState.Details(
                            UiEntity(
                                id = hotel.id,
                                name = hotel.name,
                                description = hotel.description,
                                city = hotel.city,
                                images = hotel.images,
                                starRating = "${hotel.starRating} *"
                            )

                        )
                    }
                    .onFailure {
                        _uiState.value = UiState.Error(
                            it.message ?: "Failed to fetch"
                        )
                    }
            } catch (e: Exception) {
                //throw e throw cancellation exception
                _uiState.value =
                    UiState.Error(e.message ?: "Failed to fetch")
            }
        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Error(val message: String) : UiState()
    data class Details(val hotel: UiEntity) : UiState()
}