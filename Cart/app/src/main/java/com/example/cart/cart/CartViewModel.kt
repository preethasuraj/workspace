package com.example.cart.cart

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cart.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
     val repository: CartRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = savedStateHandle.getMutableStateFlow<CartUiEntity>(
        "cart", CartUiEntity()
    )
    private val _searchText = savedStateHandle.getMutableStateFlow("search", "")

    val uiState1 = combine(_uiState, _searchText) { state, text ->
        state.item.filter { it.name.contains(text, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartUiEntity(emptyList())
    )

    @OptIn(FlowPreview::class)
    val uiState =
        combine(
            _searchText
                .debounce(300)
                .distinctUntilChanged(), _uiState
        ) { text, state ->
            val filtered = state.item.filter { it.qty > 0 && it.name.contains(text, true) }
            state.copy(
                item = filtered,
                total = filtered.sumOf { it.price * it.qty },
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = (CartUiEntity())
        )


    init {
        repository.getCartData()
            .onSuccess { result ->
                _uiState.update {
                    CartUiEntity(
                        result.items.map {
                            UiItem(
                                id = it.id,
                                name = it.name,
                                qty = it.qty,
                                price = it.price
                            )

                        },
                        searchText = _searchText.value
                    )
                }
            }
        savedStateHandle["cart"] = _uiState.value
    }

    fun onSearch(text: String) {
        _searchText.value = text
        _uiState.update {
            it.copy(
                searchText = text
            )
        }
        savedStateHandle["search"] = _searchText.value
    }

    fun onQtyChange(text: String, isIncreased: Boolean) {
        val item = _uiState.value.item.first {
            it.id == text
        }
        val updated = if (isIncreased) {
            item.copy(qty = item.qty + 1)
        } else {
            item.copy(qty = item.qty - 1)
        }

        _uiState.update {
            uiState.value.copy(
                item = _uiState.value.item.map {
                    if (it.id == text) {
                        updated
                    } else
                        it
                }
            )
        }
        savedStateHandle["cart"] = _uiState
    }



}


@Parcelize
@Immutable
data class CartUiEntity(
    val item: List<UiItem> = emptyList(),
    val searchText: String = "",
    val total: Double = 0.0
): Parcelable

@Parcelize
data class UiItem(
    val id: String,
    val name: String,
    val qty: Int,
    val price: Double,
): Parcelable