package com.mwende.onlinestore.ui.screens

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

data class RealEstateItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0
)

@Composable
fun RealEstateScreen() {
    val db = FirebaseFirestore.getInstance()
    var items by remember { mutableStateOf(listOf<RealEstateItem>()) }

    // Firestore listener
    DisposableEffect (Unit) {
        val listener: ListenerRegistration = db.collection("items")
            .whereEqualTo("category", "Real Estate")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val itemList = snapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val description = doc.getString("description") ?: ""
                    val price = doc.getDouble("price") ?: 0.0
                    RealEstateItem(doc.id, name, description, price)
                }
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
        Text("Available Real Estate", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.description)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("KES ${item.price}")
                    }
                }
            }
        }
    }
}
