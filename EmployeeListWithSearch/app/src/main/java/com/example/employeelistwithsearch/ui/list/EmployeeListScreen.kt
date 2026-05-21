package com.example.employeelistwithsearch.ui.list

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.size.Scale

@Composable
fun EmployeeListScreen(
    viewModel: EmployeeListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()

    EmployeeListScreen(
        uiState,
        searchText,
        onRefresh = { viewModel.fetchEmployees() },
        onSearch = { viewModel.updateSearch(it) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    uiState: UiState, searchText: String, onRefresh: () -> Unit, onSearch: (String) -> Unit
) {
    when (uiState) {
        UiState.Empty -> Loading()
        is UiState.Error -> ErrorScreen()
        UiState.Loading -> Loading()
        is UiState.Success -> EmployeeList(
            uiState, searchText, onRefresh, onSearch
        )
    }
}

@Composable
fun ErrorScreen() {

}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeList(
    uiState: UiState.Success, searchText: String, onRefresh: () -> Unit, onSearch: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            value = searchText,
            onValueChange = { text: String -> onSearch(text) }
        )
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxWidth(),
            isRefreshing = uiState == UiState.Loading,
            onRefresh = onRefresh
        ) {
            LazyColumn() {
                items(
                    items = uiState.employees,
                    key = { it.uuid },
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .semantics(
                                mergeDescendants = true,
                                properties = {},
                            ),
                        elevation = CardDefaults.elevatedCardElevation(3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = it.smallUrl,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentDescription = "${it.name} row",
                                contentScale = ContentScale.Crop,
                                )
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {

                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )

                            }

                        }

                    }
                }
            }
        }
    }

}