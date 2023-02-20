package com.cryptoalgo.sweetRock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Onboarding(onFinish: () -> Unit) {
    Box {
        Column {
            Text("Welcome to")
            Text("Sweet Rock", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.displayLarge)
            Text("Foods from all cultures")
            Button(onFinish) {
                Text("Let's Go")
            }
        }
    }
}