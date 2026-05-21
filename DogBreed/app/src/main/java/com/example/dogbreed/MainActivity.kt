package com.example.dogbreed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogbreed.ui.details.DetailsScreen
import com.example.dogbreed.ui.details.DetailsViewModel
import com.example.dogbreed.ui.list.DogListScreen
import com.example.dogbreed.ui.theme.DogBreedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogBreedTheme {
                NavComponent()
            }
        }
    }
}

@Composable
fun NavComponent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, Route.List.destination) {
        composable(Route.List.destination) {
            DogListScreen(
                onRowClick = { breed ->
                    navController.navigate(Route.Details.getDestination(breed))
                }
            )
        }
        composable(
            route = Route.Details.destination,
            arguments = listOf(navArgument("breed") {
                type = NavType.StringType
                defaultValue = ""

            })
        ) {
            val viewModel: DetailsViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            DetailsScreen(
                uiState = state,
                onBack = {navController.popBackStack()}
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
    DogBreedTheme {
        Greeting("Android")
    }
}

sealed class Route(val destination: String) {
    data object List : Route("List")
    data object Details : Route("Details/{breed}") {
        fun getDestination(breed: String) = "Details/${breed}"
    }

}