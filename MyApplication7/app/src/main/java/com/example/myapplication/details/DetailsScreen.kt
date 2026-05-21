package com.example.myapplication.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier) {
    Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Text",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                )
            }
        ) { padding ->


        Box(
            modifier = Modifier.padding(padding)
                .fillMaxSize()
                .background(Color.Black)


        ) {
            Row(
                modifier.fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = "Toast",
                    color = Color(0xFF404040),
                    fontSize = 16.sp,
                )
            }

            Column(
                modifier.fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp)
                    .align(Alignment.BottomStart)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        color = Color(0xFF404040),
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "$15.67",
                        color = Color(0xFF404040),
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors()
                            .copy(containerColor = Color(0xFF022CFA)),
                        modifier = Modifier.height(48.dp)
                            .weight(1f)
                            .padding(top = 8.dp)
                            .background(shape = RectangleShape, color = Color(0xFF022CFA))
                    ) {
                        Text(
                            text = "Print",
                            fontSize = 16.sp
                        )
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.height(48.dp)
                            .weight(1f)
                            .padding(top = 8.dp)
                            .background(Color(0xFF022CFA))
                    ) {
                        Text(
                            text = "Pay",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }


}

@Preview
@Composable
fun DeatilPrevie() {
    DetailsScreen()
}