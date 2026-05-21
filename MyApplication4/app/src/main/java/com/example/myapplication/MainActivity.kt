package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import com.example.myapplication.Route.Details
import com.example.myapplication.details.DetailsScreen
import com.example.myapplication.details.DetailsViewModel
import com.example.myapplication.list.UserListScreen
import com.example.myapplication.list.UserViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyAct", "onCreate hit")
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavComponent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun NavComponent(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.List.name) {
        composable(Route.List.name) {
            val vm: UserViewModel = koinViewModel()
            val uiState = vm.uiState.collectAsStateWithLifecycle()
            UserListScreen(
                uiState = uiState.value,
                onClick = { id: Int, name: String -> navController.navigate(Details.getName(id, name)) },
                paddingValues = innerPadding,
                onSearch = {text -> vm.updateSearch(text)}
            )
        }

        composable(route = Route.Details.name, arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""

        },
            navArgument("name") {
                type = NavType.StringType
                defaultValue = ""

            }, )) {
            val vm: DetailsViewModel = koinViewModel()
                DetailsScreen(
                    user = vm.getUser(),
                    onBack = { navController.popBackStack() },
                    modifier = Modifier.padding(innerPadding)
                )
        }
    }

}

sealed class Route(val name: String) {
    data object List : Route("List")
    data object Details : Route("Details/{id}/{name}") {
        fun getName(id: Int, name: String) = "Details/$id/$name"
    }
}


@Composable
fun Greeting1(modifier: Modifier = Modifier) {
    val items = listOf("one", "two", "three", "four", "five")
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        item(span = {
            GridItemSpan(maxLineSpan)
        }) {
            Text(
                "Header",
                style = MaterialTheme.typography.titleLarge
            )
        }
        items(
            items = items,
            key = { it }
        ) {
            Box(

            ) {
                Image(
                    painter = ColorPainter(Color.Yellow),
                    contentDescription = "",
                    modifier.size(80.dp)
                )
                Text(
                    text = it,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                )

            }
        }
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Text(
                "Footer",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting5(modifier: Modifier = Modifier) {
    var count = 3
    Box() {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "bell",
            Modifier.size(48.dp)
        )
//        AnimatedVisibility(
//            visible = count > 0
//        ) {
        Box(
            modifier
                .align(Alignment.TopEnd)
                .offset(x = 4.dp, y = (-4).dp)
                .defaultMinSize(18.dp, 18.dp)
                .background(Color.Red, CircleShape)
                .padding(horizontal = 5.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center


        ) {
            Text(
                text = if (count < 99) "${count}" else "${count}+",
                fontSize = 10.sp
            )
//            }
        }
    }
}

@Composable
fun Greeting3(modifier: Modifier = Modifier) {
    /*A cover image (16:9, full width, cropped).
    An 80dp circular avatar that overlaps the bottom edge of the
    cover image — half above the cover, half below. Below the avatar,
    name and handle, left-aligned.
The avatar should have a 4dp white border
(so it reads as a separate element on top of the cover).*/
    Box() {
        AsyncImage(
            model = "url",
            contentDescription = "cover",
            modifier = Modifier
                .aspectRatio(16.0f / 9),
            contentScale = ContentScale.Crop,
            error = ColorPainter(Color.Yellow),
        )
        Column(
            Modifier.padding(start = 40.dp),

            ) {
            AsyncImage(
                model = "url",
                contentDescription = "avatar",
                modifier = Modifier
                    .offset(-40.dp, -40.dp)
                    .size(80.dp),
                contentScale = ContentScale.Crop,
                error = ColorPainter(Color.Green),
            )
            Text(
                text = "Some",
            )
        }
    }

}

@Composable
fun Greeting2(modifier: Modifier = Modifier) {
    /*A 64dp circular avatar (loaded from URL),
    with a 16dp green dot positioned at the bottom-right of the avatar.
    The dot has a 2dp white border so it's visible against any avatar.
The dot must overlap the avatar — slightly hanging off, not contained
inside the circle's bounding box. */

    Box() {
        AsyncImage(
            model = "url",
            contentDescription = "avatar",
            modifier = Modifier
                .size(256.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = ColorPainter(Color.Yellow),
        )
        Box(
            modifier = Modifier
                .align(BottomEnd)
                .offset(4.dp, 4.dp)
                .size(80.dp)
                .border(8.dp, Color.White, CircleShape)
                .background(Color.Magenta, CircleShape)

        ) { }
    }
}

@Composable
fun Greeting1(name: String, modifier: Modifier = Modifier) {
    Column() {
        Box(
//            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            AsyncImage(
                model = "url",
                contentDescription = "profile",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio((16f / 9)),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.Yellow),
                error = ColorPainter(Color.Yellow),
//                error = painterResource(R.drawable.ic_launcher_background),
            )

            AsyncImage(
                model = "url",
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(BottomStart)
                    .offset(x = 40.dp, y = 40.dp)
//                    .clip(CircleShape)
                    .border(4.dp, color = Color.White, shape = CircleShape)
                    .clip(CircleShape)
                    .size(80.dp),
                error = ColorPainter(Color.Green),
                placeholder = painterResource(R.drawable.ic_launcher_background),
            )

        }

        Text(
            text = "Jane Does",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 95.dp)
        )
        Text(
            text = "Jane Does email",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 95.dp)
        )

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        //Greeting()
    }
}