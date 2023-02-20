package com.cryptoalgo.sweetRock.cart

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptoalgo.sweetRock.MainViewModel
import com.cryptoalgo.sweetRock.R

@Composable
fun Cart(
    model: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainVM: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var removingDish by remember { mutableStateOf<CartViewModel.CartItem?>(null) }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(model.cart, key = { it.food.id }) {
            CartItem(item = it) { newQuantity ->
                if (newQuantity == 0) removingDish = it
                else model.changeQuantity(it, newQuantity)
            }
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
                Button(
                    onClick = { mainVM.queueSnackbarMessage("Not implemented") },
                    Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Checkout")
                }
            }
        }
    }

    if (removingDish != null) {
        val removingFood = removingDish?.food ?: return
        AlertDialog(
            onDismissRequest = { removingDish = null },
            title = {
                Text("Remove ${removingFood.name} from cart?")
            },
            text = {
                Text("${removingFood.name} will be removed from your cart. This action cannot be undone.")
            },
            confirmButton = {
                TextButton(onClick = {
                    model.removeDish(removingDish!!)
                    removingDish = null
                }) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { removingDish = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CartItem(item: CartViewModel.CartItem, setQuantity: (Int) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.food.name, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                Text("$%.2f".format(item.food.price), style = MaterialTheme.typography.labelLarge)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Quantity:", style = MaterialTheme.typography.bodyMedium)
                FilledTonalIconButton(onClick = { setQuantity(item.quantity-1) }, Modifier.size(28.dp)) {
                    AnimatedContent(item.quantity == 1) { toRemove ->
                        Icon(
                            painterResource(if (toRemove) R.drawable.cancel else R.drawable.remove),
                            contentDescription = "Remove one",
                            Modifier.size(if (toRemove) 20.dp else 24.dp) // Visual size is different for some reason
                        )
                    }
                }
                AnimatedContent(
                    item.quantity,
                    // Taken from https://developer.android.com/jetpack/compose/animation#animatedcontent
                    transitionSpec = {
                        // Compare the incoming number with the previous number.
                        if (targetState > initialState) {
                            // If the target number is larger, it slides up and fades in
                            // while the initial (smaller) number slides up and fades out.
                            slideInVertically { height -> height } + fadeIn() with
                                    slideOutVertically { height -> -height } + fadeOut()
                        } else {
                            // If the target number is smaller, it slides down and fades in
                            // while the initial number slides down and fades out.
                            slideInVertically { height -> -height } + fadeIn() with
                                    slideOutVertically { height -> height } + fadeOut()
                        }.using(
                            // Disable clipping since the faded slide-in/out should
                            // be displayed out of bounds.
                            SizeTransform(clip = false)
                        )
                    }
                ) {
                    Text(it.toString(), fontWeight = FontWeight.Bold)
                }
                FilledTonalIconButton(onClick = { setQuantity(item.quantity+1) }, Modifier.size(28.dp)) {
                    Icon(painterResource(R.drawable.add), contentDescription = "Add one", Modifier.size(24.dp))
                }
            }
        }
    }
}