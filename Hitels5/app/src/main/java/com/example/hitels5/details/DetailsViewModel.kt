package com.example.hitels5.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hitels5.database.HotelDao
import com.example.hitels5.repository.HotelRepository
import com.example.hitels5.ui.list.UiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: HotelRepository,
    val savedStateHandle: SavedStateHandle,
    val dao: HotelDao,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    val id = savedStateHandle.get<String>("id")

    init {
        getHotel()
    }

    private fun getHotel() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                repository.getHotel(id)
                    .onSuccess {
                        val hotel = it
                        _uiState.value = UiState.ShowingDetails(hotel =
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
                        _uiState.value = UiState.Error(it.message ?: "Failed to fetch hotels")
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to fetch hotels")
            }
        }
    }
}

sealed class UiState() {
    data object Loading : UiState()
    data class Error(val message: String) : UiState()
    data class ShowingDetails(val hotel: UiEntity) : UiState()

} 