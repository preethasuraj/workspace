package com.example.cart1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cart1.timer.TimerScreen
import com.example.cart1.timer.TimerViewModel
import com.example.cart1.ui.theme.Cart1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
             val vm: TimerViewModel by viewModels ()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            Cart1Theme {
                TimerScreen(
timers = uiState,
                    onStart = {id -> vm.startTimer(id)},
                    onStop = {id -> vm.stopTimer(id)},
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Cart1Theme {
        Greeting("Android")
    }
}