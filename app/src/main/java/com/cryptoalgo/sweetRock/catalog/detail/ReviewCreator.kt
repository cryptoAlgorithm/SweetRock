package com.cryptoalgo.sweetRock.catalog.detail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptoalgo.sweetRock.MainViewModel
import com.cryptoalgo.sweetRock.catalog.CatalogViewModel
import com.cryptoalgo.sweetRock.util.SmallCircularLoader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCreator(
    item: CatalogViewModel.CatalogItem,
    mainVM: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val coroutineScope = rememberCoroutineScope()

    var rating by rememberSaveable { mutableStateOf<Int?>(null) }
    var review by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var loading by remember { mutableStateOf(false) }

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
                    loading = true
                    try {
                        item.rate(rating!!.toFloat(), review.text)
                        review = TextFieldValue("")
                        rating = null
                        mainVM.queueSnackbarMessage("Added your review!")
                    } catch (e: Exception) {
                        // TODO: Tell user about problem
                        mainVM.queueSnackbarMessage("Could not add review: ${e.localizedMessage}")
                    } finally {
                        loading = false
                    }
                }},
                Modifier
                    .fillMaxWidth()
                    .offset(y = 4.dp),
                shape = MaterialTheme.shapes.small,
                enabled = rating != null && review.text.isNotBlank() && !loading
            ) {
                if (loading) {
                    SmallCircularLoader(Modifier.padding(end = 4.dp))
                }
                Text("Post")
            }
        }
    }
}
