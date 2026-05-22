package com.preethasuraj.watchlist.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preethasuraj.watchlist.domain.model.Instrument
import com.preethasuraj.watchlist.domain.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WatchlistRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val retryTrigger = MutableStateFlow(0)

    private val debouncedQuery: Flow<String> = query
        .debounce(SEARCH_DEBOUNCE_MS)
        .map { it.trim() }
        .distinctUntilChanged()

    // flatMapLatest cancels a superseded request when the query changes or retry fires.
    private val searchPhase: Flow<SearchPhase> =
        combine(debouncedQuery, retryTrigger) { trimmedQuery, _ -> trimmedQuery }
            .flatMapLatest { trimmedQuery ->
                if (trimmedQuery.length < MIN_QUERY_LENGTH) {
                    flowOf<SearchPhase>(SearchPhase.Idle)
                } else {
                    flow<SearchPhase> {
                        emit(SearchPhase.Loading)
                        emit(
                            repository.search(trimmedQuery).fold(
                                onSuccess = { SearchPhase.Success(it) },
                                onFailure = { SearchPhase.Error(it.message) },
                            )
                        )
                    }
                }
            }

    val uiState: StateFlow<SearchUiState> = combine(
        query,
        searchPhase,
        repository.observeWatchedSymbols(),
    ) { currentQuery, phase, watchedSymbols ->
        when (phase) {
            SearchPhase.Idle -> SearchUiState(query = currentQuery)
            SearchPhase.Loading -> SearchUiState(query = currentQuery, isSearching = true)
            is SearchPhase.Success -> SearchUiState(
                query = currentQuery,
                results = phase.instruments.map {
                    SearchResultUi(it, it.symbol in watchedSymbols)
                },
            )
            is SearchPhase.Error -> SearchUiState(
                query = currentQuery,
                error = phase.message ?: DEFAULT_ERROR,
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), SearchUiState())

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }

    fun onRetry() {
        retryTrigger.value += 1
    }

    fun add(instrument: Instrument) {
        viewModelScope.launch { repository.add(instrument) }
    }

    fun remove(symbol: String) {
        viewModelScope.launch { repository.remove(symbol) }
    }

    private sealed interface SearchPhase {
        data object Idle : SearchPhase
        data object Loading : SearchPhase
        data class Success(val instruments: List<Instrument>) : SearchPhase
        data class Error(val message: String?) : SearchPhase
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
        const val MIN_QUERY_LENGTH = 1
        const val STOP_TIMEOUT_MS = 5_000L
        const val DEFAULT_ERROR = "Couldn't load results. Check your connection and try again."
    }
}
