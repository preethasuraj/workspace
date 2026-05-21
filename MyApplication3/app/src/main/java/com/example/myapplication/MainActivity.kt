package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.List.ListScreen
import com.example.myapplication.details.DetailsScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HotelAppNavGraph()
            }
        }
    }

}

@Composable
fun HotelAppNavGraph() {
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

                })) {
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
    MyApplicationTheme {
        Greeting("Android")
    }
}


sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getRoute(id: String) = "Details/${id}"
    }

}