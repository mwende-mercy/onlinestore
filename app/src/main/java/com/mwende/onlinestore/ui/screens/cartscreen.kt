package com.mwende.onlinestore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1
)

@Composable
fun CartScreen() {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var cartItems by remember { mutableStateOf(listOf<CartItem>()) }

    // Realtime cart listener
    DisposableEffect(Unit) {
        val listener = db.collection("cart")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val items = snapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val price = doc.getDouble("price") ?: 0.0
                    val quantity = doc.getLong("quantity")?.toInt() ?: 1
                    CartItem(doc.id, name, price, quantity)
                }
                cartItems = items
            }

        onDispose {
            listener.remove()
        }
    }

    val totalCost = cartItems.sumOf { it.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("🛒 Your Cart", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text("Your cart is empty", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("KES ${item.price} x ${item.quantity}")
                            }

                            Row {
                                // ➖ Decrease quantity or remove
                                IconButton(onClick = {
                                    if (item.quantity > 1) {
                                        db.collection("cart").document(item.id)
                                            .update("quantity", item.quantity - 1)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Decreased quantity", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        db.collection("cart").document(item.id).delete()
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
                                }

                                // ➕ Increase quantity
                                IconButton(onClick = {
                                    db.collection("cart").document(item.id)
                                        .update("quantity", item.quantity + 1)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Increased quantity", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show()
                                        }
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase quantity")
                                }

                                // 🗑️ Remove item directly
                                IconButton(onClick = {
                                    db.collection("cart").document(item.id).delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show()
                                        }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove item")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total
            Text("Total: KES ${"%.2f".format(totalCost)}", style = MaterialTheme.typography.titleLarge)

            Button(
                onClick = {
                    Toast.makeText(context, "Proceeding to checkout...", Toast.LENGTH_SHORT).show()
                    // You can navigate to another screen here or clear the cart
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Proceed to Checkout")
            }
        }
    }
}
