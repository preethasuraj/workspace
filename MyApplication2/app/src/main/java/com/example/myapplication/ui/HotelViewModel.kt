package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(
    val repository: HotelRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)

    private var _SearchText = MutableStateFlow("")
    val searchText = _SearchText.asStateFlow()

    fun onSearch(query: String) {
        _SearchText.value = query

    }

    @OptIn(FlowPreview::class)
    val uiState = combine(_uiState, _SearchText.debounce(100L)) { state, text ->
        when (state) {
            UiState.Loading -> state
            is UiState.Success -> {
                UiState.Success(
                    (state).hotels.filter {
                        it.name.contains(text) },
                    false
                )
            }
        }
    }.stateIn<UiState>(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        getHotels()
    }

    private fun getHotels() {
        _uiState.value = UiState.Loading
        try {
            val result = repository.loadHotels()
            _uiState.value = UiState.Success(
                result.map {
                    HotelUiEntity(
                        id = it.id,
                        name = it.name,
                        city = it.city,
                        country = it.country,
                        starRating = it.starRating,
                        description = it.description,
                        images = it.images
                    )

                },
                isRefreshing = false
            )
        } catch (e: Exception) {
            //throw e
        }

    }
}