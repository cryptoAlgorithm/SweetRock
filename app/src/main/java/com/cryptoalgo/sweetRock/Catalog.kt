package com.cryptoalgo.sweetRock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CatalogItem(val title: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Catalog() {
    val catalog = remember { listOf(
        CatalogItem("soup", "Beautiful soup you will not regret drinking!"),
        CatalogItem("rice", "The classic chinese dish"),
        CatalogItem("apples", "An apple a day keeps the doctor away"),
        CatalogItem("apples 2", "two apples a day keeps the doctor even further away!"),
        CatalogItem("oranges", "how about an orange?"),
        CatalogItem("pineapple", "pineapples on pizza are nice"),
        CatalogItem("pineapple", "pineapples on pizza are nice"),
        CatalogItem("pineapple", "pineapples on pizza are nice"),
        CatalogItem("pineapple", "pineapples on pizza are nice"),
    ) }

    LazyColumn(
        Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(16.dp)) {
        items(catalog) {
            Card(onClick = {

            }) {
                Column(modifier = Modifier.padding(12.dp, 8.dp).fillMaxWidth()) {
                    Text(text = it.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = it.description, modifier = Modifier)
                }
            }
        }
    }
}