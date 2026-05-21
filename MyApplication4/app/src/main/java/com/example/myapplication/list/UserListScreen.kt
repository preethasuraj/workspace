package com.example.myapplication.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun UserListScreen(
    paddingValues: PaddingValues,
    uiState: UiState,
    onClick: (Int, String) -> Unit,
    onSearch: (String) -> Unit
) {
    when (uiState) {
        is UiState.Error -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.message,
                    color = Color.Red
                )
            }
        }

        UiState.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.ShowingList -> {
            Column(
                modifier = Modifier.padding(paddingValues),
            ) {
                OutlinedTextField(
                    value = uiState.searchText,
                    onValueChange = { value -> onSearch(value) }
                )

                LazyVerticalGrid(
                    modifier = Modifier.padding(paddingValues),
                    columns = GridCells.Adaptive(80.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        Text(
                            "Users"
                        )

                    }
                    items(
                        items = uiState.users,
                        key = { it.id }
                    ) {
                        Column(
                            modifier = Modifier.clickable(
                                onClick = { onClick(it.id, it.name) }
                            )
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    lineBreak = LineBreak.Heading
                                )
                            )
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    lineBreak = LineBreak.Heading
                                )
                            )
                        }
                    }
                }
            }
        }
    }

}