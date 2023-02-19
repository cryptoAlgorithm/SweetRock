package com.cryptoalgo.sweetRock.catalog

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cryptoalgo.sweetRock.R
import com.cryptoalgo.sweetRock.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CatalogItemDetail(
    itemID: String,
    model: CatalogViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartVM: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
    onBack: () -> Unit,
) {
    val item = remember { model.getCatalogItem(itemID) } ?: return
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val cardOffset = LocalDensity.current.run { (-50).dp.roundToPx() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    IconButton(onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 4.dp, tonalElevation = 4.dp) {
                Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painterResource(id = R.drawable.favourite), "Favourite",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    val addedToCart = cartVM.inCart(item)
                    Button(
                        onClick = {
                            cartVM.addDish(item)
                            onBack()
                        },
                        Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small,
                        enabled = !addedToCart
                    ) {
                        Text(if (addedToCart) "Added" else "Add to Order")
                    }
                }
            }
        }
    ) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = it, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Box {
                    GlideImage(
                        model = item.coverImage, contentDescription = "",
                        Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(
                        Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    0.75f to Color.Transparent,
                                    1f to MaterialTheme.colorScheme.background
                                )
                            )
                    )
                }
            }
            // Item info card
            item {
                Box(modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .layout { measurable, constraints ->
                        val place = measurable.measure(constraints)
                        layout(place.width, place.height + cardOffset) {
                            place.placeRelative(0, cardOffset)
                        }
                    }
                ) {
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp, 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(item.name, style = MaterialTheme.typography.headlineMedium)
                            Text("$%.2f".format(item.price), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                            Text(item.description, Modifier.padding(top = 4.dp), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            // Ratings
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("What Others Think", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleLarge)
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        Column {
                            Text("4.5", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.displayMedium)
                            Text("0 Ratings", style = MaterialTheme.typography.labelMedium)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            RatingProgress(0.4f)
                            RatingProgress(0.2f)
                            RatingProgress(0.1f)
                            RatingProgress(0.05f)
                            RatingProgress(0.05f)
                        }
                    }
                }
            }
            items(count = 5) {
                OutlinedCard(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)) {
                    Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.Star, "Rating",
                                Modifier
                                    .padding(end = 4.dp)
                                    .size(24.dp),
                                tint = colorResource(R.color.star_orange)
                            )
                            Text("4.5", style = MaterialTheme.typography.labelLarge)
                        }
                        Text("This totally blew me away! amazing!")
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun RatingProgress(freq: Float) {
    LinearProgressIndicator(
        progress = freq,
        Modifier
            .height(10.dp)
            .fillMaxWidth(), strokeCap = StrokeCap.Round,
        trackColor = Color.Gray.copy(alpha = 0.15f)
    )
}