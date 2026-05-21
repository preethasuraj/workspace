package com.example.hotels3.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hotels3.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HotelListViewModel @Inject constructor(
    val repository: HotelRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState())
    private val _searchText = MutableStateFlow<String>("")

    val uiState = combine(
        _uiState,
        _searchText
            .debounce(30)
            .flatMapLatest { text-> repository.searchHotels(text) }
    ) { state, hotels ->
        if (state.listState is ListState.ShowingList) {
            state.copy(
                listState = ListState.ShowingList(hotels.map {
                    HotelUiEntity(
                        id = it.id,
                        name = it.name,
                        city = it.city,
                        description = it.description,
                        images = it.images
                    )
                }
            ),
                searchText = _searchText.value
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    init {
        getHotels()
    }

    fun getHotels() {
        _uiState.value = _uiState.value.copy(
            listState = ListState.Loading
        )
        viewModelScope.launch {
            try {
                repository.getHotels()
                    .onSuccess {
                        _uiState.update {
                           it.copy(listState = ListState.ShowingList())
                        }

                    }
                    .onFailure {
                         _uiState.update {
                            it.copy(listState = ListState.Error(""))
                        }
                    }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                } else {
                    _uiState.update {
                        it.copy(listState = ListState.Error(""))
                    }
                }
            }
        }
    }

    fun onAction(intent: Intent) {
        when (intent) {
            is Intent.Expand -> {
                _uiState.update { value ->
                    if (value.listState is ListState.ShowingList) {
                        value.copy(
                            listState = value.listState.copy(
                                expandedId =  intent.id
                            )
                        )
                    } else
                        value
                }

            }

            is Intent.Search -> {
                _searchText.value = intent.text
            }
        }

    }

}


data class UiState(
    val searchText: String = "",
    val listState: ListState = ListState.Loading
)

sealed class ListState {
    data class Error(val message: String) : ListState()
    data class ShowingList(
        val hotels: List<HotelUiEntity> = emptyList(),
        val expandedId: String? = null,
    ) : ListState()

    data object Loading : ListState()
}


data class HotelUiEntity(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    val images: List<String>,
)

sealed class Intent {
    data class Search(val text: String) : Intent()
    data class Expand(val id: String?) : Intent()
}
