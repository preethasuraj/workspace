package com.example.myapplication.timer

import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timers: List<Timer>,
    toast: Int?,
    onIntent: (i: Intent) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(toast) {
toast?.apply {
    Toast.makeText(context, "Good" ,LENGTH_SHORT ).show()
}
    }
    LazyColumn(
        modifier = modifier.padding(5.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement  = Arrangement.Center
    ) {
        item {
            androidx.compose.material3.Button(onClick = {
                onIntent(Intent.Add)
            },
               ) {
                Text(
                    text = "Add"
                )
            }
        }
        items(
            items = timers,
            key = {it.id}
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(it.duration/60)} : ${(it.duration%60)}",
                    fontSize = 30.sp
                )
                if(it.jobId == null) {
                    androidx.compose.material3.Button(onClick = {
                        onIntent(Intent.Start(it.id))
                    }) {
                        Text(
                            text = "Start"
                        )
                    }
                }else {
                    androidx.compose.material3.Button(onClick = {
                        onIntent(Intent.Pause(it.id))
                    }) {
                        Text(
                            text = "Pause"
                        )
                    }
                }
            }
        }
    }

}