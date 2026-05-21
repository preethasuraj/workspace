package com.example.vrbointerview.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel()

) {
    val timerState by viewModel.uiStateTimer.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Timers", // todo string res
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .padding(15.dp),
                elevation = CardDefaults.elevatedCardElevation(3.dp),
            ) {
                TimerComposable(timerState, 0, { id -> viewModel.onStart(id)})
                TimerComposable(timerState, 1) { id -> viewModel.onStart(id) }
                TimerComposable(timerState, 2) { id -> viewModel.onStart(id) }
                TimerComposable(timerState, 3) { id -> viewModel.onStart(id) }
            }
        }

    }

}

@Composable
fun TimerComposable(uiState: UiState, index: Int, onStart: (Int) -> Unit) {
    Column() {
        Text(
            text = "${uiState.timerState[index]}",
            style = MaterialTheme.typography.headlineSmall
        )
        Row() {
            Button(
                onClick = {onStart(index)}
            ) {
                Text(
                    text = "START",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Button(
                onClick = {}
            ) {
                Text(
                    text = "STOP",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}