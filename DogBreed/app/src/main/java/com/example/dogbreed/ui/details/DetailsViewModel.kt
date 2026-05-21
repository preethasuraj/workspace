package com.example.dogbreed.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreed.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: DogRepository,
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val breed = savedStateHandle.get<String>("breed")

    init {
        getSubBreedDetails()
    }

    private fun getSubBreedDetails() {
        viewModelScope.launch {
            if(breed != null ) {
                repository.getSubbreedDetails(breed)
                    .onSuccess {
                        _uiState.value = UiState.ShowingDetails(
                            DetailsUiEntity(
                                it.breed,
                                it.subbreed,
                                it.images
                            )
                        )
                    }
            }
            else {
                _uiState.value = UiState.Error("Empty breed")
            }
        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Error(val message: String) : UiState()
    data class ShowingDetails(val uiEntity: DetailsUiEntity) : UiState()

}

data class DetailsUiEntity(
    val breed: String,
    val subbreed: List<String>,
    val images: List<String>
)