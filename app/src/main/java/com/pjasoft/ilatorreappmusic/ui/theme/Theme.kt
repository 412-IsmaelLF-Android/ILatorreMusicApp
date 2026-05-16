package com.pjasoft.ilatorrefappmusic.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)

val Purple40 = Color(0xFF6650A4)
val PurpleGrey40 = Color(0xFF625B71)

// App Colors
val PrimaryPurple = Color(0xFF7C5CBF)
val LightPurple = Color(0xFFB39DDB)
val DeepPurple = Color(0xFF4A2D8A)
val DarkPurpleBackground = Color(0xFF1E0A3C)
val CardBackground = Color(0xFFFFFFFF)
val LightBackground = Color(0xFFF3F0F8)
val MiniPlayerBackground = Color(0xFF2D1B5E)
val OverlayPurple = Color(0x887C5CBF)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    secondary = PurpleGrey40,
    background = LightBackground,
    surface = CardBackground,
)

@Composable
fun ILatorremusicappTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
