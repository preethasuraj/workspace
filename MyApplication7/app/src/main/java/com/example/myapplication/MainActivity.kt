package com.example.myapplication

import android.R.attr.defaultValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.details.DetailsScreen
import com.example.myapplication.list.ListViewModel
import com.example.myapplication.list.UserListScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavComponent(Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun NavComponent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, Route.List.name) {
        composable(Route.List.name) {
            val vm: ListViewModel = koinViewModel()
            val uiState = vm.uiState.collectAsStateWithLifecycle()
            UserListScreen(uiState = uiState.value, modifier = modifier, onIntent = { id: Int ->
                navController.navigate(Route.Details.getName(id))
            })
        }
        composable(route = Route.Details.name, arguments = listOf(navArgument("id") {
            type = NavType.IntType
            defaultValue = -1

        })) {
            DetailsScreen()
        }
    }

}

sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}") {
        fun getName(id: Int) = "Details/${id}"
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