package com.example.hotels1.list

import androidx.lifecycle.ViewModel
import com.example.hotels1.repository.HotelListRepository
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    val repository: HotelListRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getHotels()
    }

    private fun getHotels() {
        val result = repository.getHotels()
        _uiState.value = UiState.Success(
            result
                .map {
                    HotelUiEntity(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        images = it.images,
                        starRating = "${it.starRating} *",
                    )
                })
    }

}


sealed class UiState {
    data object Loading : UiState()
    data class Success(val hotels: List<HotelUiEntity>) : UiState()
}

data class HotelUiEntity(
    val id: String,
    val name: String,
    val description: String,
    val images: List<String>,
    val starRating: String
)