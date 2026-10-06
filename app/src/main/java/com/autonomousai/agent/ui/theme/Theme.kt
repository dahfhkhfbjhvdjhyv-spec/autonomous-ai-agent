package com.autonomousai.agent.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF8B5CF6),
    secondary = androidx.compose.ui.graphics.Color(0xFF22D3EE),
    tertiary = androidx.compose.ui.graphics.Color(0xFF34D399),
    background = androidx.compose.ui.graphics.Color(0xFF0F172A),
    surface = androidx.compose.ui.graphics.Color(0xFF111827),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color(0xFFE2E8F0),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE2E8F0),
)

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF8B5CF6),
    secondary = androidx.compose.ui.graphics.Color(0xFF22D3EE),
    tertiary = androidx.compose.ui.graphics.Color(0xFF34D399),
    background = androidx.compose.ui.graphics.Color(0xFFF8FAFC),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color(0xFF0F172A),
    onSurface = androidx.compose.ui.graphics.Color(0xFF0F172A),
)

@Composable
fun AutonomousAITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
