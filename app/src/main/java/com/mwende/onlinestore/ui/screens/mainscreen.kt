package com.mwende.onlinestore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar with cart icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                navController.navigate("cart") // Replace "cart" with your actual cart screen route
            }) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "View Cart"
                )
            }
        }

        // Buttons
        Button(
            onClick = { navController.navigate("real_estate") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("🏠 Purchase Real Estate")
        }

        Button(
            onClick = { navController.navigate("shopping") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("🛍️ Online Shopping")
        }

        Button(
            onClick = { navController.navigate("add_items") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("➕ Add Products / Real Estate")
        }
    }
}

