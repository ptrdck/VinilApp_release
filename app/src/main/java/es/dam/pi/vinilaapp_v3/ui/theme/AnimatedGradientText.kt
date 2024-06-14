package es.dam.pi.vinilaapp_v3.ui.theme

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.delay

@Composable
fun AnimatedGradientText(
    text: String,
    fontFamily: FontFamily,
    fontSize: TextUnit,
    colors: List<Color>
) {
    val color = remember { Animatable(Color.White) }
    val infiniteTransition = rememberInfiniteTransition()

    val animation = infiniteTransition.animateColor(
        initialValue = Color.Transparent,
        targetValue = Color.White,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        delay(2000)
        color.snapTo(Color.White)
    }

    Box {
        GradientText(
            text = text,
            fontFamily = fontFamily,
            fontSize = fontSize,
            colors = colors,
            shadowColor = color.value
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize,
                fontFamily = fontFamily,
                brush = SolidColor(animation.value),
                shadow = Shadow(color = color.value, blurRadius = 8f)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GradientText(
    text: String,
    fontFamily: FontFamily,
    fontSize: TextUnit,
    colors: List<Color>,
    shadowColor: Color
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize,
            fontFamily = fontFamily,
            brush = Brush.radialGradient(
                colors = colors,
                center = Offset(0f, 0f),
                radius = 500f
            ),
            shadow = Shadow(
                color = shadowColor,
                blurRadius = 8f
            )
        ),
        textAlign = TextAlign.Center
    )
}
