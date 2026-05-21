package com.example.hotels3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.hotels3.Route.Details
import com.example.hotels3.ui.DetailsScreen
import com.example.hotels3.ui.DetailsViewModel
import com.example.hotels3.ui.HotelListScreen
import com.example.hotels3.ui.HotelListViewModel
import com.example.hotels3.ui.Intent
import com.example.hotels3.ui.theme.Hotels3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hotels3Theme {
                NavComponent()
            }
        }
    }
}

@Composable
fun NavComponent() {
    val navController = rememberNavController()
    val viewModel: HotelListViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    NavHost(navController, startDestination = Route.List.name) {
        composable(Route.List.name) {
            HotelListScreen(
                state,
                { intent: Intent -> viewModel.onAction(intent) },
                onRowClick = { id: String -> navController.navigate(Details.getRoute(id)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Route.Details.name, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""

        })) {
            val vm: DetailsViewModel = hiltViewModel()
            DetailsScreen(
                state = vm.uiState,
                onBack = { navController.popBackStack() }
            )
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
    Hotels3Theme {
        Greeting("Android")
    }
}


sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getRoute(id: String) = "Details/${id}"
    }
}