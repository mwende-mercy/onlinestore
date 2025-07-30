//package com.mwende.onlinestore.ui.screens
package com.mwende.onlinestore.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.mwende.onlinestore.data.Item

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun OnlineShoppingScreen() {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }

    // Load items from Firestore
    DisposableEffect(Unit) {
        val listener = db.collection("items")
            .whereEqualTo("category", "Product")
            .addSnapshotListener { snapshots, error ->
                if (error != null) return@addSnapshotListener
                val itemList = snapshots?.documents?.mapNotNull { doc ->
                    try {
                        Item(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            category = doc.getString("category") ?: ""
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                items = itemList
            }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Available Products", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No products found.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = item.description)
                            Text(text = "KES ${item.price}", style = MaterialTheme.typography.bodyLarge)

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    db.collection("cart")
                                        .whereEqualTo("name", item.name)
                                        .get()
                                        .addOnSuccessListener { querySnapshot ->
                                            if (!querySnapshot.isEmpty) {
                                                // Item exists, increase quantity
                                                val doc = querySnapshot.documents[0]
                                                val currentQuantity = doc.getLong("quantity") ?: 1
                                                db.collection("cart").document(doc.id)
                                                    .update("quantity", currentQuantity + 1)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(context, "Increased quantity in cart", Toast.LENGTH_SHORT).show()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(context, "Failed to update cart", Toast.LENGTH_SHORT).show()
                                                    }
                                            } else {
                                                // Item doesn't exist, add it
                                                val cartItem = hashMapOf(
                                                    "name" to item.name,
                                                    "price" to item.price,
                                                    "quantity" to 1
                                                )
                                                db.collection("cart").add(cartItem)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Error checking cart", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            ) {
                                Text("Add to Cart")
                            }


                        }
                    }
                }
            }
        }
    }
}

