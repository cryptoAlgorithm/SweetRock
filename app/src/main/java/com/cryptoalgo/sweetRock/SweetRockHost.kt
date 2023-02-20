package com.cryptoalgo.sweetRock

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.cryptoalgo.sweetRock.catalog.detail.CatalogItemDetail
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SweetRockHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = "home"
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            Home { navController.navigate("foodDetail/$it") }
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