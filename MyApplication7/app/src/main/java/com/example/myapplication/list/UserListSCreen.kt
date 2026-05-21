package com.example.myapplication.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun UserListScreen(
    uiState: UiState,
    onIntent: (Int) -> Unit,
    modifier: Modifier = Modifier) {
 var expanded by remember { mutableStateOf<Int?>(null) }
    when(uiState) {
        is UiState.DisplayingUsers -> {
            LazyColumn(modifier = modifier.padding(10.dp)
                .fillMaxWidth()
            ) {
                items(
                    items = uiState.users,
                    key = {it.id}
                ){
                    Card(
                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                        modifier = modifier.background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .clickable(onClick = {onIntent(it.id)})
                            .padding(10.dp)

                    ){
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = it.image,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(CircleShape).size(80.dp),
                                error = ColorPainter(androidx.compose.ui.graphics.Color.Red)
                            )
                        Text(
                            text = it.firstName + " " + it.lastName
                        )
                            if(expanded != it.id) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable(
                                        onClick = {expanded = it.id}
                                    )
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = null,
                                    modifier = Modifier.clickable(
                                        onClick = {expanded = null}
                                    )
                                )
                            }
                        }
                    }
                }
            }

        }
        is UiState.Error -> {
            Box(
                contentAlignment = Alignment.Center
            ) {
               Text(
                   text = uiState.message,
               )
            }
        }

        UiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}