package com.example.myapplication.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myapplication.List.HotelUiEntity
import com.example.myapplication.repository.HotelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: HotelsRepository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var _uiState = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val id = savedStateHandle.get<String>("id")
        getDetails(id)
    }

    private fun getDetails(id: String?) {
         val result = id?.let {
            repository.getHotel(id)
        }
        if (result != null) {
            _uiState.value = DetailsState.Details(result)
        }

    }

}

sealed class DetailsState{
    data object Loading: DetailsState()
    data class Details(val hotel: HotelUiEntity): DetailsState()
}