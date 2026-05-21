package com.example.hotels2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hotels2.details.DetailsScreen
import com.example.hotels2.list.HotelListScreen
import com.example.hotels2.ui.theme.Hotels2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hotels2Theme {
                AppNavComponent()
            }
        }
    }
}

@Composable
fun AppNavComponent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = Route.List.name
    ) {
        composable(Route.List.name) {
            HotelListScreen(
                onRowCLick = { id: String ->
                    navController.navigate(Route.Details.getDetailsRoute(id))
                }
            )
        }
        composable(
            route = Route.Details.name, arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
                defaultValue = ""

                }
            )
        ) {

            DetailsScreen(onBack = {navController.popBackStack()})
        }
    }

}

sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getDetailsRoute(id: String) = "Details/${id}"
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
    Hotels2Theme {
        Greeting("Android")
    }
}