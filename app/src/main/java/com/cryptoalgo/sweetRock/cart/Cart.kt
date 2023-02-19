package com.cryptoalgo.sweetRock.cart

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptoalgo.sweetRock.R
import com.cryptoalgo.sweetRock.catalog.CatalogViewModel

@Composable
fun Cart(
    model: CartViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(model.cart, key = { it.id }) {
            CartItem(item = it)
        }
        item {
            if (model.cart.isEmpty()) Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painterResource(id = R.drawable.cart), contentDescription = null,
                    Modifier
                        .padding(bottom = 8.dp)
                        .size(128.dp))
                Text(
                    "There's nothing in your cart!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text("Go order some delicious food :D", textAlign = TextAlign.Center)
            } else Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Divider(Modifier.padding(vertical = 4.dp))
                Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Grand Total:")
                    Spacer(Modifier.weight(1f))
                    Text("$%.2f".format(model.totalPrice), fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleLarge)
                }
                Button(onClick = { }, Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                    Text(text = "Checkout")
                }
            }
        }
    }
}

@Composable
private fun CartItem(item: CatalogViewModel.CatalogItem) {
    Card {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(item.name, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Text("$%.2f".format(item.price), style = MaterialTheme.typography.labelLarge)
        }
    }
}