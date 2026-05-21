package com.example.cart.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartUiEntity: CartUiEntity,
    onSearch: (String) -> Unit,
    onQtyChange: (String, Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cart",
                        style = typography.headlineMedium
                    )
                }
            )
        }
    ) { paddingValues ->
        Cart(
            Modifier.padding(paddingValues),
            cartUiEntity,
            onSearch,
            onQtyChange,
        )

    }

}

@Composable
fun Cart(
    modifier: Modifier,
    cartUiEntity: CartUiEntity,
    onSearch: (String) -> Unit,
    onQtyChange: (String, Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = cartUiEntity.searchText,
            onValueChange = { value -> onSearch(value) }
        )
        Spacer(
            modifier = Modifier
                .padding(5.dp)
        )
        LazyColumn(
            modifier = Modifier
                .padding(5.dp)
        ) {
            items(
                items = cartUiEntity.item,
                key = { it.id },
            ) {
                ItemRow(it, onQtyChange)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = typography.headlineSmall
            )
            Text(
                text = "${cartUiEntity.total}",
                style = typography.headlineSmall
            )
        }

    }
}

@Composable
fun ItemRow(item: UiItem, onQtyChange: (String, Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),

            ) {
            Text(
                text = item.name,
                style = typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "-",
                style = typography.headlineSmall,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(
                        onClick = { onQtyChange(item.id, false) }
                    )
            )
            Text(
                text = "${item.qty}",
                modifier = Modifier
                    .padding(10.dp),
                style = typography.headlineSmall,
            )
            Text(
                text = "+",
                style = typography.headlineSmall,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(
                        onClick = { onQtyChange(item.id, true) }
                    )
            )


        }
    }
}