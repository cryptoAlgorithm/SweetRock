package com.cryptoalgo.sweetRock.util

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun SmallCircularLoader(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier.size(16.dp),
        strokeWidth = 2.dp,
        strokeCap = StrokeCap.Round
    )
}