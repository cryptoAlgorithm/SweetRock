package com.cryptoalgo.sweetRock

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import com.cryptoalgo.sweetRock.account.AccountLanding
import com.cryptoalgo.sweetRock.catalog.Catalog
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun Home(navigateToItemDetail: (String) -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val items = remember { listOf(Pair("Catalog", R.drawable.food_menu), Pair("Cart", R.drawable.cart), Pair("You", R.drawable.account)) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(item.second), contentDescription = item.first) },
                        label = { Text(item.first) },
                        selected = pagerState.targetPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) }}
                    )
                }
            }
        },
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Sweet Rock")
                        AnimatedContent(targetState = items[pagerState.targetPage].first) { page ->
                            Text(page, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            var clickCount by remember { mutableStateOf(0) }
            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Snackbar # ${++clickCount}"
                        )
                    }
                }
            ) { Text("Show snackbar") }
        },
        content = { innerPadding ->
            HorizontalPager(
                pageCount = items.count(),
                contentPadding = innerPadding,
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> Catalog(navigateToDetail = { navigateToItemDetail(it) })
                    2 -> AccountLanding()
                    else -> Text(
                        text = "Page: $page",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    )
}