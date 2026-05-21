package com.example.hitels5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hitels5.details.DetailScreen
import com.example.hitels5.details.DetailsViewModel
import com.example.hitels5.ui.list.ListScreen
import com.example.hitels5.ui.list.ListViewModel
import com.example.hitels5.ui.theme.Hitels5Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hitels5Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationComponent()
                }
            }
        }
    }
}

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    NavHost(navController, Route.List.destination) {
        composable(Route.List.destination) {
            val vm: ListViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            ListScreen(uiState = uiState, onRowClick = {id: String ->
                navController.navigate(Route.Details.getDetailsRoute(id))
            })
        }
        composable(route = Route.Details.destination, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""

        })) {
            val vm: DetailsViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            DetailScreen(uiState = uiState, onBack = {navController.popBackStack()})
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
    Hitels5Theme {
        Greeting("Android")
    }
}

sealed class Route(val destination: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getDetailsRoute(id: String) = "Details/${id}"
    }
}