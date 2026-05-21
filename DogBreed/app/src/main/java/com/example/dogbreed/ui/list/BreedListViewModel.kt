package com.example.dogbreed.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreed.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    val repository: DogRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    init {
        getDogs()
    }

    private fun getDogs() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getDogBreeds()
                .onSuccess {
                    _uiState.value =
                        UiState.ShowingList(UiEntity(
                            dogBreeds = it.message.keys.toList(),
                            dogSubBreeds = it.message
                        ))

                }
                .onFailure {
                    _uiState.value = UiState.Error(it.message?: "Failed")
                }


        }
    }


}

sealed class UiState{
    data object Loading: UiState()
    data class Error(val message: String): UiState()
    data class ShowingList(val uiEntity: UiEntity): UiState()

}

data class UiEntity(
    val dogBreeds: List<String>,
    val dogSubBreeds: Map<String, List<String>>
)