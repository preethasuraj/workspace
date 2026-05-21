package com.example.dogbreed

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogbreed.Route.Details
import com.example.dogbreed.breed.list.DogBreedListViewModel
import com.example.dogbreed.breed.list.DogBreedScreen
import com.example.dogbreed.ui.theme.DogBreedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: DogBreedListViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            NavComponent()
        }
    }
}

@Composable
fun NavComponent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, Route.List.destination) {
        composable(Route.List.destination) {
            val vm: DogBreedListViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            DogBreedScreen(
                uiState = uiState,
                onBreedSelect = { id ->
                    navController.navigate(Details.getDestination(id))
                }
            )
        }
        composable(route = Route.Details.destination, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""

        })) {
            //DetsilsScreen()

        }
    }

}

sealed class Route(val destination: String) {
    data object List : Route("List")
    data object Details : Route("Details/id") {
        fun getDestination(id: String) = "${Route.Details.destination}/id"
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