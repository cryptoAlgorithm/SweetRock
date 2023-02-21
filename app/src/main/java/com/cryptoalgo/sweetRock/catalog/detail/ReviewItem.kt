package com.cryptoalgo.sweetRock.catalog.detail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptoalgo.sweetRock.MainViewModel
import com.cryptoalgo.sweetRock.R
import com.cryptoalgo.sweetRock.model.Rating
import kotlinx.coroutines.launch

@Composable
fun ReviewItem(
    review: Rating,
    canDelete: Boolean,
    mainVM: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val coroutineScope = rememberCoroutineScope()
    var deleting by remember { mutableStateOf(false) }

    var confirmPresented by remember { mutableStateOf(false) }

    OutlinedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.small
    ) {
        Column(Modifier.padding(top = 4.dp, start = 12.dp, end = 8.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 1..5) {
                    Icon(
                        Icons.Rounded.Star, "Rating",
                        Modifier.size(16.dp),
                        tint = if (review.rating >= i) colorResource(R.color.star_orange) else LocalContentColor.current.copy(alpha = 0.2f)
                    )
                }
                val added = review.formatTimestamp()
                if (added != null) Text(added, Modifier.padding(start = 4.dp), style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.weight(1f))
                if (canDelete) IconButton(
                    onClick = { confirmPresented = true },
                    Modifier.size(32.dp),
                    enabled = !deleting
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete rating")
                } else Spacer(Modifier.height(32.dp)) // To ensure a constant height
            }
            Text(review.review, Modifier.padding(end = 4.dp))
        }
    }

    if (confirmPresented) {
        AlertDialog(
            onDismissRequest = { confirmPresented = false },
            title = {
                Text("Delete review?")
            },
            text = {
                Text("This will permanently delete your review posted on ${review.formatTimestamp()}")
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmPresented = false
                    deleting = true
                    coroutineScope.launch {
                        try {
                            review.delete()
                            mainVM.queueSnackbarMessage("Deleted rating!")
                        } catch (e: Exception) {
                            mainVM.queueSnackbarMessage("Could not delete rating:\n${e.localizedMessage}")
                        } finally { deleting = false }
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmPresented = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
