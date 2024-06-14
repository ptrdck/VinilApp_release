package es.dam.pi.vinilaapp_v3.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = RetroWarmPrimary,
    secondary = RetroWarmSecondary,
    background = RetroWarmBackground,
    surface = Color.White,
    onPrimary = ButtonText,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = RetroWarmPrimary,
    secondary = RetroWarmSecondary,
    background = Color.DarkGray,
    surface = Color.DarkGray,
    onPrimary = ButtonText,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun VinilaAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val backgroundBrush = if (darkTheme) {
        Brush.radialGradient(
            colors = listOf(Color(0xFFC70039), Color(0xFF000000), Color(0xFF3C3C3C)),
            center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
            radius = 1000f
        )
    } else {
        Brush.linearGradient(
            colors = listOf(RetroWarmBackground, Color.White)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize() // Asegura que Surface llene todo el espacio
                    .background(brush = backgroundBrush) // Aplica el fondo aquí
            ) {
                content()
            }
        }
    )
}

@Composable
fun LightOnlyTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(RetroWarmBackground, Color.White)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize() // Asegura que Surface llene todo el espacio
                    .background(brush = backgroundBrush) // Aplica el fondo aquí
            ) {
                content()
            }
        }
    )
}
