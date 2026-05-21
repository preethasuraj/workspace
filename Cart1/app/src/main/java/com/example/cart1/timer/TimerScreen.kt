package com.example.cart1.timer

import android.R.attr.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    timers: List<Timer>,
    onStart: (Long) -> Unit,
    onStop: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Timers",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        LazyColumn(
            modifier = modifier.padding(5.dp)
        ) {
            items(
                items = timers,
                key = { it.id }
            ) {
                TimerRow(it, onStart, onStop)
            }
        }
    }

}

@Composable
fun TimerRow(timer: Timer, onStart: (Long) -> Unit, onStop: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(5.dp)
        ) {
            Text(
                text = "${timer.duration}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {onStart(timer.id)}) {
                Text(
                    text = "START",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Button(onClick = {onStop(timer.id)}) {
                Text(
                    text = "STOP",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}