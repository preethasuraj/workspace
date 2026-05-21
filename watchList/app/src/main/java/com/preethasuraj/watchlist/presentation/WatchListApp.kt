package com.preethasuraj.watchlist.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.preethasuraj.watchlist.presentation.search.SearchScreen
import com.preethasuraj.watchlist.presentation.watchlist.WatchlistScreen

private enum class Screen { Watchlist, Search }

/**
 * Top-level navigation. With only two screens, a saved enum + BackHandler keeps things
 * simple and avoids a navigation dependency; this would become a Navigation-Compose
 * NavHost if the screen graph grew. Each screen resolves its own ViewModel via
 * hiltViewModel(), scoped to the hosting activity.
 */
@Composable
fun WatchListApp() {
    var screen by rememberSaveable { mutableStateOf(Screen.Watchlist) }

    BackHandler(enabled = screen == Screen.Search) { screen = Screen.Watchlist }

    when (screen) {
        Screen.Watchlist -> WatchlistScreen(onSearch = { screen = Screen.Search })
        Screen.Search -> SearchScreen(onBack = { screen = Screen.Watchlist })
    }
}
