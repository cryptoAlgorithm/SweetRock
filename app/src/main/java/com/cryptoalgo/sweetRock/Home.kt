package com.cryptoalgo.sweetRock

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryptoalgo.sweetRock.account.AccountLanding
import com.cryptoalgo.sweetRock.cart.Cart
import com.cryptoalgo.sweetRock.cart.CartViewModel
import com.cryptoalgo.sweetRock.catalog.Catalog
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun Home(
    model: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartVM: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
    navigateToItemDetail: (String) -> Unit,
    navigateToAbout: () -> Unit,
    navigateToOnboarding: () -> Unit
) {
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
                        // Include badge indicating number of items in cart
                        icon = {
                            BadgedBox(badge = {
                                if (index == 1) Badge { Text("${cartVM.cart.size}") }
                            }) {
                                Icon(painterResource(item.second), item.first)
                            }
                        },
                        label = { Text(item.first) },
                        selected = pagerState.settledPage == index,
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
                        AnimatedContent(targetState = items[pagerState.settledPage].first) { page ->
                            Text(page, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(model.snackbarHostState) },
        content = { innerPadding ->
            HorizontalPager(
                pageCount = items.count(),
                contentPadding = innerPadding,
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> Catalog(navigateToDetail = navigateToItemDetail)
                    1 -> Cart()
                    2 -> AccountLanding(
                        navigateToOnboarding = navigateToOnboarding,
                        navigateToAbout = navigateToAbout
                    )
                }
            }
        }
    )
}