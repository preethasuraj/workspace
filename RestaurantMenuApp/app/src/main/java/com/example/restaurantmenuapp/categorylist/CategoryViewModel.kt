package com.example.restaurantmenuapp.categorylist

import android.icu.text.LocaleDisplayNames
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.example.restaurantmenuapp.local.CategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoriesRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() {
        repository.getCategories()
            .onSuccess { category ->
                _uiState.value = UiState.ShowingList(
                    category.categories.map { category ->
                        UiCategory(
                            id = category.id,
                            name = category.name,
                            items = category.items.map{
                                UiItem(
                                    id = it.id,
                                    name = it.name,
                                    price = it.price,
                                    image = it.image
                                )
                            }
                        )
                    }
                )
            }
            .onFailure {
                _uiState.value = UiState.Error(it.message ?: "")
            }
    }
}


sealed class UiState{
    data object Loading: UiState()
    data class ShowingList(
        val uiCategories: List<UiCategory>
    ): UiState()
    data class Error(
        val message: String
    ): UiState()

}



@Immutable
data class UiCategory(
    val id: String,
    val name: String,
    val items: List<UiItem>,
)

@Immutable
data class UiItem(
    val id: String,
    val name: String,
    val price: String,
    val image: String,

)