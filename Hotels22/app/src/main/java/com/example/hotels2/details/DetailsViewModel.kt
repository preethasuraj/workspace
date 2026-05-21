package com.example.hotels2.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hotels2.list.UiEntity
import com.example.hotels2.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: HotelRepository,
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val id = savedStateHandle.get<String>("id")
        getDetails(id)
    }

    private fun getDetails(id: String?) {
        id?.let {
            repository.getDetails(id).onSuccess {
                _uiState.value = UiState.Details(
                    UiEntity(
                        id = it.id,
                        name = it.name,
                        city = it.city,
                        description = it.description,
                        starRating = "${it.starRating}",
                        images = it.images
                    )
                )
            }
        }
    }
}

sealed class UiState{
    data object Loading: UiState()
    data class Details(val hotel: UiEntity): UiState()
}