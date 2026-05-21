package com.example.restaurantapp.list

import androidx.lifecycle.ViewModel
import com.example.restaurantapp.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MenuListViewModel @Inject constructor(
    private val repository: MenuRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
getData()
    }

    private fun getData() {
        try {
            _uiState.value = UiState.Loading
            repository.getMenu().onSuccess { categories ->

                _uiState.value = UiState.ShowingMenu(categories.map { category ->
                    UiCategory(
                        id = category.id,
                        name = category.name,
                        items = category.items.map {
                            UiItem(
                                id = it.id,
                                name = it.name,
                                url = it.image
                            )
                        }
                    )
                })
            }
        } catch (e: Exception) {
            // if cancellation
            //_uiState.value = UiState.Error
            throw e
        }
    }


}

sealed class UiState {
    data object Loading : UiState()
    data class ShowingMenu(
        val categoryList: List<UiCategory>
    ) : UiState()
}

data class UiCategory(
    val id: String,
    val name: String,
    val items: List<UiItem>,
)

data class UiItem(
    val id: String,
    val name: String,
    val url: String,
)
