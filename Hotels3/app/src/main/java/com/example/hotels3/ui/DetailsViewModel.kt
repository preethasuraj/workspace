package com.example.hotels3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hotels3.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val repository: HotelRepository,
) : ViewModel() {
    val id = savedStateHandle.get<String>("id")
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getDetails()
    }
    fun getDetails() {
         try {
            repository.getDetails(id)
                .onSuccess {
                    _uiState.value = DetailsUiState.DetailsState(
                        HotelUiEntity(
                            id = it.id,
                            name = it.name,
                            city = it.city,
                            description = it.description,
                            images = it.images
                        )
                    )
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data class DetailsState(val hotelUiEntity: HotelUiEntity) : DetailsUiState()
}