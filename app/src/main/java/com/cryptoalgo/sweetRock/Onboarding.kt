package com.cryptoalgo.sweetRock

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Onboarding(onFinish: () -> Unit) {
    var welcomeVisible by remember { mutableStateOf(false) }
    var titleVisible by remember { mutableStateOf(false) }
    var goVisible by remember { mutableStateOf(false) }

    LaunchedEffect(null) {
        val handler = Handler(Looper.getMainLooper())
        welcomeVisible = true
        handler.postDelayed({
            titleVisible = true
            handler.postDelayed({
                goVisible = true
            }, 2000)
        }, 1200)
    }

    Box(contentAlignment = Alignment.Center) {
        Box(Modifier.align(Alignment.BottomCenter)) {
            Image(
                painterResource(R.drawable.onboarding_background),
                null,
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.background,
                            0.25f to Color.Transparent
                        )
                    )
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(welcomeVisible, enter = fadeIn(tween(1000)) + expandVertically()) {
                Text(
                    "Welcome to",
                    textAlign = TextAlign.Center
                )
            }
            AnimatedVisibility(
                titleVisible,
                enter = fadeIn(tween(1000)) + expandVertically(spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow))
            ) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Sweet Rock",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold, style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        "Experience a world of flavors with our multicultural menu",
                        Modifier.padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            AnimatedVisibility(
                goVisible,
                enter = fadeIn(tween(1000)) + expandVertically(spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow))
            ) {
                Button(onFinish) {
                    Text("Let's Go")
                    Icon(Icons.Rounded.ArrowForward, contentDescription = null, Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}