package com.liceo.prelim.profilecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GroceryListApp()
            }
        }
    }
}

@Composable
fun GroceryListApp() {
    // ---- 1. STATE ----
    // The current text in the input box
    var newItem by remember { mutableStateOf("") }
    // The list of grocery items (observable, so the UI updates)
    val groceries = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "My Grocery List", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // ---- 2. INPUT + ADD EVENT ----
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newItem,
                onValueChange = { newItem = it },
                label = { Text("Enter an item") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                // Challenge 1: Ignore empty items and trim
                if (newItem.isNotBlank()) {
                    groceries.add(newItem.trim())
                    newItem = "" // clear the input box
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Challenge 2: Show a live item count
        Text(text = "Total items: ${groceries.size}", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Optional bonus: Clear All button
        if (groceries.isNotEmpty()) {
            Button(
                onClick = { groceries.clear() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Clear All")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---- 3. LIST + DELETE EVENT ----
        LazyColumn {
            items(groceries) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item, fontSize = 18.sp)
                    IconButton(onClick = { groceries.remove(item) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
