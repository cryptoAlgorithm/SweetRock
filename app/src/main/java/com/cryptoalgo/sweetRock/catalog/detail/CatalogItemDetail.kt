package com.cryptoalgo.sweetRock.catalog.detail

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cryptoalgo.sweetRock.MainViewModel
import com.cryptoalgo.sweetRock.R
import com.cryptoalgo.sweetRock.account.AccountViewModel
import com.cryptoalgo.sweetRock.cart.CartViewModel
import com.cryptoalgo.sweetRock.catalog.CatalogViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CatalogItemDetail(
    itemID: String,
    model: CatalogViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartVM: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
    authVM: AccountViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainVM: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
    onBack: () -> Unit,
) {
    val item = remember { model.getCatalogItem(itemID) } ?: return
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val cardOffset = LocalDensity.current.run { (-50).dp.roundToPx() }

    // We aren't using a view model here because I have no idea how to implement one in this scenario
    val ratings = remember { mutableStateListOf<Rating>() }
    val ratingFrequency = RatingUtil.ratingFrequency(ratings)
    // Attach a listener to the reviews collection with a filter
    LaunchedEffect(null) {
        Firebase.firestore
            .collection("ratings")
            .whereEqualTo("foodID", itemID)
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    ratings.clear()
                    value.forEach { ratings.add(it.toObject()) }
                    ratings.sortByDescending { it.timestamp }
                }
            }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    IconButton(onBack) {
                        Icon(Icons.Rounded.Close, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(mainVM.snackbarHostState) },
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
                            mainVM.queueSnackbarMessage("Added ${item.name} to cart!")
                            onBack()
                        },
                        Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small,
                        enabled = !addedToCart
                    ) {
                        Text(if (addedToCart) "Added to Order" else "Add to Order")
                    }
                }
            }
        }
    ) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = it, verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                    // Use a funky custom layout to overlay the card on the cover image
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

            // Review Creator
            item {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Leave a review",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (authVM.user == null) {
                        Text(text = "You'll need to be signed in to leave a review")
                    } else ReviewCreator(item)
                }
            }

            // Reviews
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Ratings", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleLarge)
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        Column {
                            Text(
                                if (ratings.isNotEmpty()) "%.1f".format(RatingUtil.average(ratings)) else "-",
                                fontWeight = FontWeight.Bold, style = MaterialTheme.typography.displayMedium
                            )
                            Text(
                                "${ratings.size} Ratings",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (i in 5 downTo 1) {
                                val target by animateFloatAsState(targetValue = ratingFrequency[i] ?: 0f)
                                RatingProgress(target)
                            }
                        }
                    }
                }
            }
            items(ratings, key = { rating -> rating.id ?: "" }) { review -> ReviewItem(review) }
            if (ratings.isEmpty()) item {
                Text(
                    "There are currently no ratings for this dish. You could be the first to write one!",
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ReviewItem(
    review: Rating,
) {
    OutlinedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.small
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 1..5) {
                    Icon(
                        Icons.Rounded.Star, "Rating",
                        Modifier
                            .size(16.dp),
                        tint = if (review.rating > i) colorResource(R.color.star_orange) else LocalContentColor.current.copy(alpha = 0.2f)
                    )
                }
                val added = review.formatTimestamp()
                if (added != null) Text(added, Modifier.padding(start = 4.dp), style = MaterialTheme.typography.labelMedium)
            }
            Text(review.review)
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