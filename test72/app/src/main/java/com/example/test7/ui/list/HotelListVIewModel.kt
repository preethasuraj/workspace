package com.example.test7.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test7.repository.DomainEntity
import com.example.test7.repository.HotelRepository
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.map

@HiltViewModel
class HotelListVIewModel @Inject constructor(
    val repository: HotelRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = combine(_uiState, repository.hotels) { state, hotels ->
        if (state is UiState.ShowingList) {
            state.copy(
                hotels = hotels.map {
                    it.toUiEntity()
                }.filter { it.name.contains(state.searchText, true) }
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        getHotels()
    }

    fun getHotels() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                repository.getHotelsRemote()
                    .onSuccess { result ->
                        _uiState.value = UiState.ShowingList()
                    }
                    .onFailure {
                        _uiState.value = UiState.Error(it.message ?: "")
                    }

            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                } else {
                    _uiState.value = UiState.Error(e.message ?: "")
                }
            }
        }
    }

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.Expand -> {
                _uiState.update { value ->
                    if (value is UiState.ShowingList) {
                        if (value.expandedId == intent.id) {
                            value.copy(expandedId = null)
                        } else {
                            value.copy(expandedId = intent.id)
                        }
                    } else {
                        value
                    }

                }

            }

            is Intent.Search -> {
                _uiState.update { value ->
                    if (value is UiState.ShowingList) {
                        value.copy( searchText = intent.text)
                    } else {
                        value
                    }

                }

            }
        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Error(val message: String) : UiState()
    data class ShowingList(
        val hotels: List<UiEntity> = emptyList(),
        var expandedId: String? = null,
        val searchText: String = "",
    ) : UiState()
}

data class UiEntity(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    @SerializedName("star_rating")
    val starRating: String,
    val images: List<String>
)

fun DomainEntity.toUiEntity(): UiEntity {
    return UiEntity(
        id = this.id,
        name = this.name,
        city = this.city,
        description = this.description,
        starRating = "${this.starRating} *",
        images = this.images,
    )
}

sealed class Intent {
    data class Expand(val id: String) : Intent()
    data class Search(val text: String) : Intent()
}