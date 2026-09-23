package edu.liceo.fieldkit.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import edu.liceo.fieldkit.hardware.currentLocation

// GIVEN (read it, do not change it)
fun locationAccess(context: Context): String {
    fun has(p: String) =
        ContextCompat.checkSelfPermission(context, p) == PERMISSION_GRANTED
    return when {
        has(ACCESS_FINE_LOCATION) -> "Precise"
        has(ACCESS_COARSE_LOCATION) -> "Approximate"
        else -> "None"
    }
}

@Composable
fun LocationCard() {
    val context = LocalContext.current
    var access by remember { mutableStateOf(locationAccess(context)) }
    var text by remember { mutableStateOf("Tap the button to tag your location.") }
    LifecycleResumeEffect(Unit) { // GIVEN: re-check after Settings
        access = locationAccess(context)
        onPauseOrDispose { }
    }
    // TODO 11a: val launcher = rememberLauncherForActivityResult(
    // ActivityResultContracts.RequestMultiplePermissions()) { result -> ... }
    // TODO 11b: inside: access = "Precise" if result[FINE] == true,
    // "Approximate" if result[COARSE] == true, otherwise "None"
    // TODO 11c: inside: if access is "None", set text to
    // "Location denied. Allow it in Settings."
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        access = when {
            result[ACCESS_FINE_LOCATION] == true -> "Precise"
            result[ACCESS_COARSE_LOCATION] == true -> "Approximate"
            else -> "None"
        }
        if (access == "None") {
            text = "Location denied. Allow it in Settings."
        }
    }

    Card(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Where am I?", style = MaterialTheme.typography.titleMedium)
            Text("Location access: $access")
            Button(onClick = {
                // TODO 12a: if access == "None", launch with BOTH permissions
                if (access == "None") {
                    launcher.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
                } else {
                    // TODO 12b: else call context.currentLocation { loc -> ... }
                    context.currentLocation { loc ->
                        // TODO 12c: inside 12b, set text to the coordinates,
                        // or to a message when loc is null
                        text = loc?.let { "%.5f, %.5f (±%.0f m)".format(it.latitude, it.longitude, it.accuracy) }
                            ?: "No fix yet. Is Location turned on?"
                    }
                }
            }) { Text("Tag my location") }
            Text(text)
        }
    }
}
