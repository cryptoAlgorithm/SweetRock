package com.cryptoalgo.sweetRock.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cryptoalgo.sweetRock.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CatalogItemDetail(
    itemID: String,
    model: CatalogViewModel = viewModel(),
    onBack: () -> Unit,
) {
    var catalogItem by remember { mutableStateOf<CatalogViewModel.CatalogItem?>(null) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // Get the selected catalog item here
    // Doesn't work if it's retrieved during calculation of remember { }
    LaunchedEffect(model.catalog) { catalogItem = model.getCatalogItem(itemID) }

    val cardOffset = LocalDensity.current.run { (-50).dp.roundToPx() }

    val item = catalogItem ?: return
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
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(id = R.drawable.favourite), "Favourite",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Button(
                        onClick = {  },
                        Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text("Add to Order")
                    }
                }
            }
        }
    ) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = it) {
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
                            Text(item.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            items(count = 100) {
                Text(text = "b")
            }
        }
    }
}