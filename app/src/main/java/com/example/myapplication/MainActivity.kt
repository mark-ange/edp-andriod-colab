package com.example.myapplication

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.example.myapplication.ui.theme.MyApplicationTheme

object Brand {
    val Background = Color(0xFFF0F2F5)
    val AccentColor = Color(0xFF771C1B) // Warm red tone matching Gaara's theme
    val AccentLight = Color(0xFFFBEBEA)
    val DarkBackground = Color(0xFF121212)
    val GradientStart = Color(0xFF9E2A2B)
    val GradientEnd = Color(0xFF541212)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        SingletonImageLoader.setSafe { context ->
            coil3.ImageLoader.Builder(context)
                .components {
                    add(OkHttpNetworkFetcherFactory())
                }
                .build()
        }

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Brand.Background
                    ) {
                        val gaaraImageUrl = "https://images.wallpapersden.com/image/download/gaara-naruto_bGdoZWyUmZqaraWkpJRpbmtmrWhqZW0.jpg"
                        BusinessCard(profileImageUrl = gaaraImageUrl)
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessCard(profileImageUrl: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brand.Background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Brand.GradientStart, Brand.GradientEnd)
                            )
                        )
                )


                Box(
                    modifier = Modifier.height(280.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(360.dp)
                            .align(Alignment.Center)
                            .offset(y = (-70).dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(6.dp)
                    ) {
                        if (!profileImageUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = profileImageUrl,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Brand.AccentColor)
                            ) {
                                Text(
                                    text = "AM",
                                    color = Color.White,
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                // Details Column
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Abellano Mark Angelou",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2B2B2B)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brand.AccentLight)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "STUDENT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Brand.AccentColor,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Contact List Box
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color(0xFFFAFAFA))
                            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
                            .padding(8.dp)
                    ) {
                        ContactRow(
                            iconSymbol = "📞",
                            label = "+63 900 000 0000"
                        )
                        ContactRow(
                            iconSymbol = "✉️",
                            label = "abellano.mark@example.com"
                        )
                        ContactRow(
                            iconSymbol = "👤",
                            label = "FB: Mark Angelou Abellano"
                        )
                        ContactRow(
                            iconSymbol = "📸",
                            label = "IG: @mark_abellano"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactRow(iconSymbol: String, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { }
            .padding(vertical = 20.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Brand.AccentLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconSymbol,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(36.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333)
        )
    }
}

@Preview(name = "Card - Light", showBackground = true, widthDp = 560)
@Composable
fun BusinessCardPreview() {
    MyApplicationTheme {
        BusinessCard(profileImageUrl = "https://images.wallpapersden.com/image/download/gaara-naruto_bGdoZWyUmZqaraWkpJRpbmtmrWhqZW0.jpg")
    }
}

@Preview(name = "Card - Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES, widthDp = 560)
@Composable
fun BusinessCardDarkPreview() {
    MyApplicationTheme {
        Box(modifier = Modifier.background(Brand.DarkBackground)) {
            BusinessCard(profileImageUrl = "https://images.wallpapersden.com/image/download/gaara-naruto_bGdoZWyUmZqaraWkpJRpbmtmrWhqZW0.jpg")
        }
    }
}