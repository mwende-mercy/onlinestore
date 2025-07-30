package com.mwende.onlinestore.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.mwende.onlinestore.screens.RealEstateScreen
import com.mwende.onlinestore.ui.auth.LoginScreen
//import com.mwende.onlinestore.ui.auth.SignupScreen
import com.mwende.onlinestore.ui.auth.SignupScreen
import com.mwende.onlinestore.ui.screens.AddItemsScreen
import com.mwende.onlinestore.ui.screens.CartScreen
import com.mwende.onlinestore.ui.screens.MainScreen
import com.mwende.onlinestore.ui.screens.OnlineShoppingScreen
import com.mwende.onlinestore.ui.screens.RealEstateScreen
//import com.mwende.onlinestore.ui.screens.ShoppingScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Main : Screen("main")
    object RealEstate : Screen("real_estate")
    object Shopping : Screen("shopping")
    object AddItems : Screen("add_items")
    object Cart: Screen("cart")
}


@Composable
fun NavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(onNavigateToLogin = {
                navController.popBackStack()
            })
        }
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.RealEstate.route) {
            RealEstateScreen()
        }
        composable(Screen.Shopping.route) {
            OnlineShoppingScreen()
        }
        composable(Screen.AddItems.route) {
            AddItemsScreen()
        }
        composable(Screen.Cart.route) {
            CartScreen()
        }

    }
}
