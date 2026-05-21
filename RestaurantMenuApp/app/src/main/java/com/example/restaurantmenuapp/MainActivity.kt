package com.example.restaurantmenuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmenuapp.categorylist.CategoryViewModel
import com.example.restaurantmenuapp.categorylist.MenuScreen
import com.example.restaurantmenuapp.ui.theme.RestaurantMenuAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantMenuAppTheme {
                NavComponent()
            }
        }
    }
}

@Composable
fun NavComponent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val vm: CategoryViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = Route.List.destination
    ) {
        composable(Route.List.destination){
            MenuScreen(
                uiState = uiState
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
    RestaurantMenuAppTheme {
        Greeting("Android")
    }
}

sealed class Route(val destination: String) {
    data object List: Route("List")
    data object Details: Route("Details/{id}")
//    {
//        fun getDestination(): String = destination
//    }

}