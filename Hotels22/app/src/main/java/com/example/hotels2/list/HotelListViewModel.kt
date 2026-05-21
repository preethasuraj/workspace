package com.example.hotels2.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotels2.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelListViewModel @Inject constructor(
    val repository: HotelRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow<HotelListState>(HotelListState())
    private var searchText = MutableStateFlow<String>("")
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val uiState = combine(
        _uiState,
        searchText
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query: String->
                repository.getHotels(query)
            }
    ) { state, hotels ->
        state.copy(
            hotels = hotels.map {
                UiEntity(
                    id = it.id,
                    name = it.name,
                    city = it.city,
                    description = it.description,
                    starRating = "${it.starRating}",
                    images = it.images
                )
            }
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        HotelListState()
    )

    init {
        getHotels()
    }

    fun onIntent(intent: UiIntent) {
        when (intent) {
            UiIntent.ClearSearch -> updateSearch("")
            is UiIntent.ExpandHotel -> expand(intent.id)
            UiIntent.Refresh -> getHotels()
            is UiIntent.Search -> updateSearch(intent.text)
        }
    }

    fun updateSearch(text: String) {
        searchText.update {
           text
        }
    }

    fun expand(id: String) {
        _uiState.update {
            it.copy(expandedId = id)
        }
    }

    fun getHotels() {
        viewModelScope.launch {
            repository.getHotelsRemote().onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false

                    )
                }
            }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            error = e.message

                        )
                    }
                }
        }
    }
}


data class HotelListState(
    val loading: Boolean = true,
    val error: String? = null,
    val hotels: List<UiEntity> = emptyList(),
    val expandedId: String = "",
)


data class UiEntity(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    val starRating: String,
    val images: List<String>
)

sealed class UiIntent {
    data class Search(val text: String) : UiIntent()
    data class ExpandHotel(val id: String) : UiIntent()
    data object ClearSearch : UiIntent()
    data object Refresh : UiIntent()
}