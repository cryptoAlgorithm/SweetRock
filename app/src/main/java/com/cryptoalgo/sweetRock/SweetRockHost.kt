package com.cryptoalgo.sweetRock

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
            Home(navigateToItemDetail = {
                navController.navigate("foodDetail/$it")
            })
        }
        composable("foodDetail/{id}") {
            Text(text = it.arguments!!.getString("id")!!)
        }
    }
}