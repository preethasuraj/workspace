package com.preethasuraj.watchlist.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preethasuraj.watchlist.domain.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: WatchlistRepository,
) : ViewModel() {

    val uiState: StateFlow<WatchlistUiState> = combine(
        repository.observeWatchlist(),
        repository.connectionState,
    ) { items, connection ->
        WatchlistUiState(items = items, isLoading = false, connection = connection)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = WatchlistUiState(isLoading = true),
    )

    fun remove(symbol: String) {
        viewModelScope.launch { repository.remove(symbol) }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
