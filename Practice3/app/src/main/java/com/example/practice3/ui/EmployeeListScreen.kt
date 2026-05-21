package com.example.practice3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.practice3.R
import com.example.practice3.network.Employee
import kotlinx.coroutines.flow.StateFlow
import org.intellij.lang.annotations.JdkConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(uiState: StateFlow<UiState>, onRefresh: () -> Unit) {
   val screeState by uiState.collectAsStateWithLifecycle()
    val isRefreshing = screeState == UiState.Loading

    when(val state = screeState){
        UiState.Empty -> {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = "Empty",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }
        is UiState.Error ->  Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.bodyMedium,
            )

            Button(onRefresh) {
                Text(stringResource(R.string.retry))
            }
        }

        UiState.Loading -> {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                CircularProgressIndicator()
            }

        }
        is UiState.Success -> {
            PullToRefreshBox(
                onRefresh = onRefresh,
                isRefreshing = isRefreshing
            ) {
                LazyColumn() {
                    items(
                        items = state.employees,
                        key = {it.id}
                    ){
                        EmployeeRow(it)
                    }
                }
            }
        }
    }


}

@Composable
fun EmployeeRow(employee: Employee) {
   Card(
       modifier = Modifier
           .fillMaxWidth()
           .semantics(mergeDescendants = true) {
               contentDescription = ""
           }
           .padding(5.dp),
       elevation = CardDefaults.cardElevation(3.dp),

   ){
       Row(
           modifier = Modifier.padding(2.dp),
           verticalAlignment = Alignment.CenterVertically
       ) {
           AsyncImage(
               model = employee.smallUrl,
               contentScale = ContentScale.Crop,
               contentDescription = "Image${employee.name}",
               modifier = Modifier
                   .clip(CircleShape)
                   .size(50.dp)
           )
           Column(
               modifier = Modifier.padding(2.dp).weight(0.5••f),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally,
           ) {
               Text("${employee.name}",
                   style = MaterialTheme.typography.bodyMedium)
               Text("${employee.email}",
                   style = MaterialTheme.typography.bodySmall)

           }

       }

   }
}

