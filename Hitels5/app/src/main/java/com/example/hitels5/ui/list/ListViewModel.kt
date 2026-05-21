package com.example.hitels5.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hitels5.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    val repository: HotelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val hotels = repository.hotels
    val uiState = combine(_uiState, hotels) { state, hotels ->
        when {
            state is UiState.ShowingList -> {
                state.copy(hotels = hotels.map {
                    UiEntity(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        city = it.city,
                        images = it.images,
                        starRating = "${it.starRating}"
                    )
                })
            }
            else ->{
                state
            }

        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )


        init {
            getHotels()
        }

        private fun getHotels() {
            _uiState.value = UiState.Loading
            viewModelScope.launch {
                try {
                    repository.getHotels()
                        .onSuccess {
                            _uiState.value = UiState.ShowingList(
                                hotels = emptyList()
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
        data class ShowingList(val hotels: List<UiEntity>) : UiState()

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
