package com.cryptoalgo.sweetRock

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About(onBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("About Sweet Rock") },
                navigationIcon = {
                    IconButton(onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painterResource(R.drawable.large_icon), null,
                    Modifier
                        .padding(top = 16.dp)
                        .clip(MaterialTheme.shapes.large)
                        .width(180.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
            item {
                Text(
                    stringResource(R.string.app_name),
                    Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium
                )
            }
            item {
                Text(
                    stringResource(R.string.sweet_rock_description),
                    Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            item {
                OutlinedCard(Modifier.padding(16.dp, 8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 12.dp),
                    ) {
                        Text("Contact", Modifier.padding(bottom = 4.dp), style = MaterialTheme.typography.titleLarge)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Email: info@srock.com")
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:")
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf("info@srock.com"))
                                    putExtra(Intent.EXTRA_SUBJECT, "Re: Sweet Rock")
                                }
                                try {
                                    startActivity(context, intent, null)
                                } catch (_: Throwable) {}
                            }, Modifier.size(36.dp)) {
                                Icon(Icons.Rounded.Email, contentDescription = "Open email client")
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Phone: +65 8124 0500")
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = {
                                startActivity(
                                    context,
                                    Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:+6581240500") },
                                    null
                                )
                            }, Modifier.size(36.dp)) {
                                Icon(Icons.Rounded.Phone, contentDescription = "Open dialer")
                            }
                        }
                    }
                }
            }
        }
    }
}