package com.example.test7

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
import com.example.test7.ui.details.DetailsScreen
import com.example.test7.ui.details.DetailsViewModel
import com.example.test7.ui.list.HotelListScreen
import com.example.test7.ui.list.HotelListVIewModel
import com.example.test7.ui.theme.Test7Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test7Theme {
                NavGraph()
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

@Composable
fun NavGraph() {
    val navHostController = rememberNavController()

    NavHost(navHostController, Route.List.name) {
        composable(Route.List.name) {
            val vIewModel: HotelListVIewModel = hiltViewModel()
            val uiState by vIewModel.uiState.collectAsStateWithLifecycle()
            HotelListScreen(
                uiState = uiState,
                onRowClick = { id -> navHostController.navigate(Route.Details.getRoute(id)) },
                onAction = {intent -> vIewModel.onIntent(intent)}
            )
        }
        composable(route = Route.Details.name, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""

        })) {
            val vIewModel: DetailsViewModel = hiltViewModel()
            val uiState by vIewModel.uiState.collectAsStateWithLifecycle()
            DetailsScreen(uiState = uiState, onBack = {navHostController.popBackStack()})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Test7Theme {
        Greeting("Android")
    }
}

sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getRoute(id: String) = "Details/${id}"
    }
}