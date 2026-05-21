package com.example.hotels4

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
import com.example.hotels4.ui.details.DetailsScreen
import com.example.hotels4.ui.details.DetailsViewModel
import com.example.hotels4.ui.list.ListScreen
import com.example.hotels4.ui.list.ListViewModel
import com.example.hotels4.ui.theme.Hotels4Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hotels4Theme {
                NavComposable()
            }
        }
    }
}

@Composable
fun NavComposable(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, Route.List.name) {
        composable(Route.List.name) {
            val vm: ListViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            ListScreen(uiState = uiState, onRowClick = {
                id : String -> navController.navigate(Route.Details.getRoute(id))
            })
        }
        composable(route = Route.Details.name, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""

        }
        )
        ) {
            val vm: DetailsViewModel = hiltViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            DetailsScreen(uiState = uiState, onBack = { navController.popBackStack() })
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
    Hotels4Theme {
        Greeting("Android")
    }
}

sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getRoute(id: String): String = "Details/${id}"
    }
}