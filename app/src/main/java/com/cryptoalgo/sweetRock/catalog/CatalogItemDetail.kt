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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cryptoalgo.sweetRock.R
import com.cryptoalgo.sweetRock.cart.CartViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

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

    // We aren't using a view model here because I have no idea how to implement one in this scenario
    val ratings = remember { mutableStateListOf<Rating>() }
    // Attach a listener to the reviews collection with a filter
    LaunchedEffect(null) {
        Firebase.firestore
            .collection("ratings")
            .whereEqualTo("foodID", itemID)
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    ratings.clear()
                    value.forEach { ratings.add(it.toObject()) }
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

            item {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Rate this dish",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleLarge
                    )
                    RatingCreator(item)
                }
            }
            // Ratings
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("What others think", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleLarge)
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
            items(ratings) {
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
                            Text("%.1f".format(it.rating), style = MaterialTheme.typography.labelLarge)
                        }
                        Text(it.review)
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RatingCreator(item: CatalogViewModel.CatalogItem) {
    val coroutineScope = rememberCoroutineScope()

    var rating by rememberSaveable { mutableStateOf<Int?>(null) }
    var review by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    ElevatedCard {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            StarRater(rating ?: 0) { rating = it }
            OutlinedTextField(
                review, onValueChange = { review = it }, Modifier.fillMaxWidth(),
                label = { Text("Write a review") }
            )
            FilledTonalButton(
                onClick = { coroutineScope.launch {
                    try {
                        item.rate(rating!!.toFloat(), review.text)
                        review = TextFieldValue("")
                        rating = null
                    } catch (e: Exception) {
                        // TODO: Tell user about problem
                    }
                }},
                Modifier
                    .fillMaxWidth()
                    .offset(y = 4.dp),
                shape = MaterialTheme.shapes.small,
                enabled = rating != null && review.text.isNotBlank()
            ) {
                Text("Post")
            }
        }
    }
}

@Composable
private fun StarRater(rating: Int, onRate: (Int) -> Unit) {
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