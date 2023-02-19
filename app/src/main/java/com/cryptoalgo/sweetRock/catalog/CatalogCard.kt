package com.cryptoalgo.sweetRock.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cryptoalgo.sweetRock.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CatalogCard(
    item: CatalogViewModel.CatalogItem,
    navigateToDetail: (itemID: String) -> Unit
) {
    ElevatedCard(
        onClick = { navigateToDetail(item.id) },
    ) {
        item.coverImage?.let {
            GlideImage(
                model = it, contentDescription = null,
                Modifier.fillMaxWidth()
            )
        }
        Column(
            Modifier
                .padding(12.dp, 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$%.2f".format(item.price), style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Rounded.Star, "Rating",
                    Modifier
                        .size(20.dp)
                        .padding(end = 2.dp),
                    tint = colorResource(R.color.star_orange)
                )
                Text("4.5", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}