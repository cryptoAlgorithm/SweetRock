package com.cryptoalgo.sweetRock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Catalog(navigateToDetail: (itemID: String) -> Unit) {
    val catalog = remember { listOf(
        CatalogProvider.CatalogItem(
            UUID.randomUUID(),
            "soup",
            "Beautiful soup you will not regret drinking!"
        ),
        CatalogProvider.CatalogItem(UUID.randomUUID(), "rice", "The classic chinese dish"),
        CatalogProvider.CatalogItem(
            UUID.randomUUID(),
            "apples",
            "An apple a day keeps the doctor away"
        ),
        CatalogProvider.CatalogItem(
            UUID.randomUUID(),
            "apples 2",
            "two apples a day keeps the doctor even further away!"
        ),
        CatalogProvider.CatalogItem(UUID.randomUUID(), "oranges", "how about an orange?"),
        CatalogProvider.CatalogItem(UUID.randomUUID(), "pineapple", "pineapples on pizza are nice"),
        CatalogProvider.CatalogItem(UUID.randomUUID(), "pineapple", "pineapples on pizza are nice"),
        CatalogProvider.CatalogItem(UUID.randomUUID(), "pineapple", "pineapples on pizza are nice"),
        CatalogProvider.CatalogItem(UUID.randomUUID(), "pineapple", "pineapples on pizza are nice"),
    ) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(catalog.count()) {
            val item = catalog[it]
            Card(
                onClick = {
                    navigateToDetail(item.id.toString())
                },
                colors = CardDefaults.outlinedCardColors(),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier
                    .padding(12.dp, 8.dp)
                    .fillMaxWidth()
                ) {
                    Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = item.description, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}