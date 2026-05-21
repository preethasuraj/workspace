package com.example.myapplication.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.remote.CartDetails
import com.example.myapplication.remote.Product
import com.example.myapplication.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartListViewModel @Inject constructor(
    val repository: CartRepository,
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    private val _loading = MutableStateFlow<Boolean>(true)
    val uiState = combine(_error, _loading, repository.cartDetails) { error, loading, cartDetails ->
        when {
            loading -> UiState.Loading
            error != null -> UiState.Error(error)
            cartDetails.isEmpty() -> UiState.Empty
            else -> {
                val result = cartDetails.map { cart ->
                    val productsList =
                        mutableMapOf<Int, com.example.myapplication.database.Product>()
                    cart.products.forEach { product ->
                        var p = productsList.getOrDefault(
                            product.id, null
                        )
                        if (p == null
                        ) {
                            productsList[product.id] = product
                        } else {
                            p = p.copy(
                                qty = p.qty + product.qty
                            )
                            productsList[product.id] = p

                        }
                    }
                    cart.copy(
                        products = productsList.values.map { it }
                    )

                }

                UiState.DisplayingList(
                    result
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        getCartDetails()
    }

    fun getCartDetails() {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.getCartDetails()
                    .onSuccess {
                        _loading.value = false
                    }
                    .onFailure {
                        Log.d("preetha", "getCartDetails: ${it.message}")
                        _loading.value = false
                        _error.value = it.message ?: "Error"
                    }
            } catch (e: Exception) {
                Log.d("preetha", "getCartDetails: ${e.message}")
                _error.value = e.message ?: "Error"
            }
        }
    }

}


sealed class UiState {
    data object Loading : UiState()
    data object Empty : UiState()

    data class DisplayingList(
        val uiCart: List<com.example.myapplication.database.CartDetails>
    ) : UiState()

    data class Error(val message: String) : UiState()
}