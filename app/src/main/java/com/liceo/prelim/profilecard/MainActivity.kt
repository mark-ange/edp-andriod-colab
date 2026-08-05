package com.liceo.prelim.profilecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liceo.prelim.profilecard.ui.theme.ProfileCardLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileCardLabTheme {
                GroceryListApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListApp() {
    var newItem by remember { mutableStateOf("") }
    val groceries = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Grocery Smart", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    if (groceries.isNotEmpty()) {
                        IconButton(onClick = { groceries.clear() }) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = "Clear All",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // ---- Input Section Card ----
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItem,
                        onValueChange = { newItem = it },
                        label = { Text("Add new item...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (newItem.isNotBlank()) {
                                groceries.add(newItem.trim())
                                newItem = ""
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ---- Header / Stats ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Items",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                AssistChip(
                    onClick = { },
                    label = { Text("Total: ${groceries.size}") },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ---- List Section ----
            if (groceries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "List is empty",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(groceries) { item ->
                        GroceryItemRow(
                            item = item,
                            onDelete = { groceries.remove(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryItemRow(item: String, onDelete: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
