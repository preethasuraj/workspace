package com.example.myapplication.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartScreen(
    uiState: UiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is UiState.DisplayingList -> {
            LazyColumn(
                modifier = modifier.padding(10.dp),
            ) {
                uiState.uiCart.forEach { cart ->

                    stickyHeader(key = cart.cartEntity.id) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                        )
                        {
                            Text(
                                text = "Total: ${cart.cartEntity.total}",
                                modifier = Modifier.align(Alignment.End)
                            )

                        }
                    }
                    items(
                        items = cart.products,
                        key = { "${cart.cartEntity.id}-${it.id}" }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = it.title,
                            )
                            Text(
                                text = "Total: $${"%.2f".format(it.price)}",
                            )
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
                    text = uiState.message
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

        UiState.Empty -> {

        }
    }
}