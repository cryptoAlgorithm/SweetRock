package com.cryptoalgo.sweetRock.catalog.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.cryptoalgo.sweetRock.R

@Composable
fun StarRater(rating: Int, onRate: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        for (i in 1..5) {
            IconButton(onClick = { onRate(i) }) {
                val active = rating >= i
                Icon(
                    painterResource(if (active) R.drawable.star_filled else R.drawable.star_outlined),
                    "Star: $i",
                    tint = if (active) colorResource(R.color.star_orange) else LocalContentColor.current
                )
            }
        }
    }
}