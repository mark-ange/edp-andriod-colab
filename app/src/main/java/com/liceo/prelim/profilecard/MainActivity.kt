package com.liceo.prelim.profilecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.liceo.liceochat.ui.ChatScreen
import com.liceo.prelim.profilecard.ui.theme.ProfileCardLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileCardLabTheme {
                Surface {
                    ChatScreen()
                }
            }
        }
    }
}
