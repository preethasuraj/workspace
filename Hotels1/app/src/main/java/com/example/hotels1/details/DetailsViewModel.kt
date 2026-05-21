package com.example.hotels1.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hotels1.list.HotelUiEntity
import com.example.hotels1.repository.HotelListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: HotelListRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _uiState = MutableStateFlow<UiState>(
        UiState.Loading
    )

    val uiState = _uiState.asStateFlow()

    init {
        getDetails()
    }

    fun getDetails() {
        val id = savedStateHandle.get<String>("id")
        val result = repository.getDetails(id)
        _uiState.value = com.example.hotels1.details.UiState.Details(
            HotelUiEntity(
                id = result.id,
                name = result.name,
                description = result.description,
                images = result.images,
                starRating = "${result.starRating}*"
            )
        )

    }

}

sealed class UiState {
    data object Loading : UiState()
    data class Details(val entity: HotelUiEntity) : UiState()
}