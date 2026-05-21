package com.example.dogbreed.breed.list

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreed.repository.DogBreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogBreedListViewModel @Inject constructor(
    private val repository: DogBreedRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getDogBreedCategories()
    }

    private fun getDogBreedCategories() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getDogBreed()
                .onSuccess {
                    _uiState.value = UiState.ShowingList(
                        DogBreedUiEntity(
                            it
                        )
                    )
                }
                .onFailure {
                    _uiState.value = UiState.Error(it.message ?: "")
                }
        }
    }
}

@Immutable
data class DogBreedUiEntity(
    val dogBreed: Map<String, List<String>>
)

sealed class UiState {
    data object Loading : UiState()
    data class Error(val message: String) : UiState()
    data class ShowingList(val dogBreedToSubbreedMap: DogBreedUiEntity) : UiState()
}