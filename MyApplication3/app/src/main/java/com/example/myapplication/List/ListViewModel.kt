package com.example.myapplication.List

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.HotelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    val repository: HotelsRepository
) : ViewModel() {

    private var _searchText = MutableStateFlow<String>("")
    val searchText = _searchText.asStateFlow()
    private var _uiState = MutableStateFlow<ListUiState>(
        ListUiState.Loading
    )
    val uiState = combine(
        _searchText, _uiState
    ) { searchText, uiState ->
        when (uiState) {
            ListUiState.Loading -> uiState
            is ListUiState.Success ->
                ListUiState.Success(uiState.list.filter {
                    it.name.contains(searchText, ignoreCase = true) ||
                            it.description.contains(searchText, ignoreCase = true)
                })
        }


    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(50000),
        initialValue = ListUiState.Loading

    )

    init {
        getHotels()
    }

    fun updateSearch(text: String) {
        _searchText.value = text
    }

    fun getHotels() {
        repository.getHotelsFromLocal()
            .onSuccess { result ->
                _uiState.value = ListUiState.Success(
                    result.map {
                        HotelUiEntity(
                            id = it.id,
                            name = it.name,
                            city = it.city,
                            images = it.images,
                            description = it.description,
                            starRating = "${it.starRating} *"
                        )
                    }
                )
            }
    }


}