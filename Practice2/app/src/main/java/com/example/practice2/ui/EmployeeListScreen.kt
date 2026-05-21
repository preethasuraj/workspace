@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.practice2.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.practice2.R
import com.example.practice2.network.Employee
import com.example.practice2.network.EmployeesList

@Composable
fun EmployeeListScreen(vm: EmployeeListViewModel = hiltViewModel(), onRowClick: (String) -> Unit) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val refreshing by vm.refreshing.collectAsStateWithLifecycle()

    EmployeeScreenStateless(uiState, { vm.fetchData() }, refreshing, onRowClick)
}

@Composable
fun EmployeeScreenStateless(
    uiState: UiState,
    onRefresh: () -> Unit,
    refreshing: Boolean,
    onRowClick: (String) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = onRefresh,
        modifier = Modifier
            .fillMaxSize()
    ) {
        println("preetha $uiState")
        when (uiState) {
            UiState.Empty -> EmptyList()
            is UiState.Error -> ErrorScreen(onRefresh)
            UiState.Loading -> LoadingScreen()
            is UiState.Success -> EmployeeList(uiState.employees, onRowClick)
        }

    }
}

@Composable
fun EmptyList() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.empty),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ErrorScreen(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.empty),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onRefresh) {
            Text(
                stringResource(R.string.retry),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

}


@Composable
fun EmployeeList(
    employees: EmployeesList,
    onRowClick: (String) -> Unit = {}
) {
    LazyColumn(modifier = Modifier
        .padding(5.dp)) {
        items(
            items = employees.employees,
            key = {it.uuid}){
            EmployeeRow(it, onRowClick)
        }

    }
}

@Composable
fun EmployeeRow(
    employee: Employee , onRowClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .semantics(mergeDescendants = true, properties = {}),
        elevation = CardDefaults.cardElevation(3.dp),
        onClick = { onRowClick(employee.uuid) }
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            AsyncImage(
                model = employee.smallUrl,
                contentDescription = "Row of employee ${employee.name}",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_android_black_24dp),
                error = painterResource(R.drawable.ic_android_black_24dp),
                modifier = Modifier.size(70.dp)
                    .clip(shape = CircleShape),
            )
            Column(modifier = Modifier
                .padding(5.dp),
                verticalArrangement = Arrangement.Center){
                Text(
                    employee.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    employee.email,
                    style = MaterialTheme.typography.titleSmall
                )
            }

        }
    }
}