package com.example.test6

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test6.ui.DetailsScreen
import com.example.test6.ui.PropertyListScreen
import com.example.test6.ui.theme.Test6Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test6Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PropertyNavGraph(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun PropertyNavGraph(
        modifier: Modifier
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = PropertyListRoute
        ) {
            composable<PropertyListRoute> {
                PropertyListScreen(modifier,
                    { id ->
                        navController.navigate(PropertyDetailsRoute(id))
                    }
                    )
            }
        }
    }
}

@Serializable
data object PropertyListRoute

@Serializable
data class PropertyDetailsRoute(
    val id: String
)