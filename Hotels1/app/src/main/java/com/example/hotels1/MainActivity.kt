package com.example.hotels1

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
import com.example.hotels1.details.DetailsScreen
import com.example.hotels1.list.ListScreen
import com.example.hotels1.ui.theme.Hotels1Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hotels1Theme {
                HotelAppNav()
            }
        }
    }
}

@Composable
fun HotelAppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.List.name) {
        composable(Route.List.name) {
            ListScreen(
                onRowClick = { id: String -> navController.navigate(Route.Details.getRoute(id)) }
            )
        }
        composable(route = Route.Details.name, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""
        }
        )
        ) {
            DetailsScreen()
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
    Hotels1Theme {
        Greeting("Android")
    }
}


sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getRoute(id: String) = "Details/${id}"
    }
}