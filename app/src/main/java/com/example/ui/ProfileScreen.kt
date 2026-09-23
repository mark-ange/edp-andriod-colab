package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.User
import java.util.Calendar

@Composable
fun ProfileScreen(user: User, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "You successfully logged in!",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
                Text(text = "Welcome back, ${user.fullName}.")
            }
        }

        Text("My Profile", style = MaterialTheme.typography.headlineSmall)

        ProfileRow("Full name", user.fullName)
        ProfileRow("Email", user.email)
        ProfileRow("Birthdate", user.birthdate)

        // TODO 14d: Show calculated age if birthdate is valid
        ageFrom(user.birthdate)?.let { age ->
            ProfileRow("Age", "$age years old")
        }

        ProfileRow("User ID", user.id)

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log out")
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

// TODO 14a, 14b, 14c: Calculate age from YYYY-MM-DD string
fun ageFrom(birthdate: String): Int? {
    val parts = birthdate.split("-")
    if (parts.size != 3) return null

    val y = parts[0].toIntOrNull() ?: return null
    val m = parts[1].toIntOrNull() ?: return null
    val d = parts[2].toIntOrNull() ?: return null

    val now = Calendar.getInstance()
    val ty = now.get(Calendar.YEAR)
    val tm = now.get(Calendar.MONTH) + 1
    val td = now.get(Calendar.DAY_OF_MONTH)

    var age = ty - y
    if (tm < m || (tm == m && td < d)) {
        age -= 1
    }
    return age
}
