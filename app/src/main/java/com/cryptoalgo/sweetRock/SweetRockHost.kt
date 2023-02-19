package com.cryptoalgo.sweetRock

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cryptoalgo.sweetRock.catalog.CatalogItemDetail

@Composable
fun SweetRockHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "home"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            Home { navController.navigate("foodDetail/$it") }
        }
        composable("foodDetail/{id}") {
            CatalogItemDetail(itemID = it.arguments!!.getString("id")!!) {
                navController.popBackStack("home", false)
            }
        }
    }
}