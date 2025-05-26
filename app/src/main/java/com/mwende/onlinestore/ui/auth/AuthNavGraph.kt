package com.mwende.onlinestore.ui.auth

//package com.yourname.onlinestore.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mwende.onlinestore.ui.auth.SignupScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
}

@Composable
fun AuthNavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(onNavigateToSignup = {
                navController.navigate(Screen.Signup.route)
            })
        }
        composable(Screen.Signup.route) {
            SignupScreen(onNavigateToLogin = {
                navController.popBackStack()
            })
        }
    }
}
