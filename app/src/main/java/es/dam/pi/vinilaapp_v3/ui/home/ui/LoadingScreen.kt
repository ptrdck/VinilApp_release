package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.widget.ImageView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import es.dam.pi.vinilaapp_v3.R
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(onTimeout: () -> Unit = {}) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))

    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            delay(50)
            progress += 0.01f
        }
        onTimeout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GifImage(
            imageResId = R.drawable.loading_gf, // Asegúrate de que este sea el archivo GIF
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Cargando...",
            fontSize = 24.sp,
            fontFamily = myFontFamily,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.alpha(alpha)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(progress = progress)
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Diseño y desarrollo por @ptrdck",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.vinilofont)),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GifImage(modifier: Modifier, imageResId: Int) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                Glide.with(context)
                    .asGif()
                    .load(imageResId)
                    .into(this)
            }
        }
    )
}
