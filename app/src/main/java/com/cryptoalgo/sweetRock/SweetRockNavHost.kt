package com.cryptoalgo.sweetRock

import android.content.Context
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.navigation.NavHostController
import com.cryptoalgo.sweetRock.catalog.detail.CatalogItemDetail
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

private const val ONBOARDING_SEEN_KEY = "onboarding_seen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SweetRockHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("cryptoalgo.sweetRock", Context.MODE_PRIVATE) }

    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (!prefs.getBoolean(ONBOARDING_SEEN_KEY, false)) "onboarding" else "home",
    ) {
        composable(
            "home",
            exitTransition = {
                fadeOut(tween(200))
            }
        ) {
            Home(
                navigateToItemDetail = { navController.navigate("foodDetail/$it") },
                navigateToAbout = { navController.navigate("about") },
                navigateToOnboarding = {
                    prefs.edit { remove(ONBOARDING_SEEN_KEY) }
                    navController.navigate("onboarding") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            "about",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                )
            }
        ) {
            About { navController.popBackStack("home", false) }
        }
        composable(
            "onboarding",
            enterTransition = {
                scaleIn(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow), 2f) + fadeIn(tween(250))
            },
            exitTransition = { fadeOut(tween(1000)) + scaleOut(tween(750), 2f) }
        ) {
            Onboarding {
                prefs.edit { putBoolean(ONBOARDING_SEEN_KEY, true) }
                navController.navigate("home") {
                    popUpTo(0)
                }
            }
        }
        composable(
            "foodDetail/{id}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Up,
                    spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Down,
                    spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                )
            }
        ) {
            CatalogItemDetail(itemID = it.arguments!!.getString("id")!!) {
                navController.popBackStack("home", false)
            }
        }
    }
}