package com.example.hotels4.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotels4.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    val repository: HotelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getHotels()
    }

    private fun getHotels() {
        viewModelScope.launch {
            try {
                repository.getHotels()
                    .onSuccess { domainEntities ->
                        _uiState.value = UiState.ShowingList(
                            hotel = domainEntities.map {
                                UiEntity(
                                    id = it.id,
                                    name = it.name,
                                    description = it.description,
                                    city = it.city,
                                    images = it.images,
                                    starRating = "${it.starRating} *"
                                )
                            }
                        )
                    }
                    .onFailure {
                        _uiState.value = UiState.Error(it.message ?: "Failed to fetch")
                    }
            } catch (e: Exception) {
                //throw e throw cancellation exception
                _uiState.value = UiState.Error(e.message ?: "Failed to fetch")
            }
        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Error(val message: String) : UiState()
    data class ShowingList(val hotel: List<UiEntity>) : UiState()
}

data class UiEntity(
    val id: String,
    val name: String,
    val description: String,
    val city: String,
    val images: List<String>,
    val starRating: String
) {

}


