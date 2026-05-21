package com.example.toastinterview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toastinterview.Greeting
import com.example.toastinterview.ui.theme.ToastInterviewTheme

@Composable
fun PaymentScreen() {
    Scaffold(
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        Column(
            modifier = modifier.fillMaxSize().background(Color(0xFF000000)),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            HeaderRow(modifier)
            FooterRow()
        }

    }
}


@Composable
fun HeaderRow(modifier: Modifier) {
    Box(
        modifier
            .fillMaxWidth()

            .background(Color(0xFFFFFFFF))
    ) {
        Text(
            text = "Toast",
            color = Color(0xFF404040),
            fontSize = 16.sp,
            modifier = Modifier.padding(12.dp)
        )
    }
}


@Composable
fun FooterRow() {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total:",
                color = Color(0xFF404040),
                fontSize = 16.sp
            )
            Text(
                text = "$15.67",
                color = Color(0xFF404040),
                fontSize = 16.sp
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {},
                shape = RectangleShape,
                modifier = Modifier
                    .background(Color(0xFF022CFA))
                    .height(48.dp)
                    .size(150.dp)
            ) {
                Text(
                    text = "Print",
                    color = Color(0xFFFFFFFF),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .background(Color(0xFF022CFA))
                    .height(48.dp)
                    .size(150.dp)
            ) {
                Text(
                    text = "Pay $15.67",
                    color = Color(0xFFFFFFFF),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    ToastInterviewTheme {
        PaymentScreen()
    }
}