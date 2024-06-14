package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.ui.theme.VinilaAppTheme
import es.dam.pi.vinilaapp_v3.viewmodel.NewsViewModel

@Composable
fun NewsScreen(viewModel: NewsViewModel, isDarkTheme: Boolean) {
    val newsImages by viewModel.newsImages.collectAsState()
    var expandedImageUrl by remember { mutableStateOf<String?>(null) }

    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))
    val myFontFamily2 = FontFamily(Font(resId = R.font.vinilofont2))

    VinilaAppTheme(darkTheme = isDarkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "Últimas Novedades",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 40.sp,
                        fontFamily = myFontFamily,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No te pierdas los conciertos de esta semana",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = myFontFamily,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )

                }

                items(newsImages) { imageUrl ->
                    NewsCard(imageUrl = imageUrl, onImageClick = { expandedImageUrl = imageUrl })
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¡Más novedades de Los Vinilos y Comunidad Vinilos en general.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = myFontFamily2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Pronto!! Nuevas funciones en VinilApp! Conocerás las listas musicales de vinilos y de sus miembros! Recolecta tus momentos Vinilos en tu GaleríaVinila y podrás participar en concursos y ganar premios!!!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = myFontFamily2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SocialLinksSection()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "R u Ready, Vinilo?",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        fontFamily = myFontFamily,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }

            if (expandedImageUrl != null) {
                ImageDialog(imageUrl = expandedImageUrl) { expandedImageUrl = null }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsCard(imageUrl: String, onImageClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onImageClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}

@Composable
fun SocialLinksSection() {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "¿Quieres más? ¡¡Conecta con nuestras RRSS y nuestra web!!",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SocialLinkButton(
                text = "Spotify",
                url = "https://open.spotify.com/playlist/5uhgM0E1z0g2XdHzcv9VAi?si=4beKyy9yQZODnIPla0tMCg&pi=e-WGzIQTciTlST",
                iconRes = R.drawable.ic_spotify,
                context = context
            )
            SocialLinkButton(
                text = "Facebook",
                url = "https://www.facebook.com/comunidadvinilos/",
                iconRes = R.drawable.ic_facebook,
                context = context
            )
            SocialLinkButton(
                text = "Instagram",
                url = "https://www.instagram.com/comunidadvinilos/",
                iconRes = R.drawable.ic_instagram,
                context = context
            )
            SocialLinkButton(
                text = "Web",
                url = "https://www.comunidadvinilos.com/",
                iconRes = R.drawable.ic_comunidadvinilos,
                context = context
            )
        }
    }
}

@Composable
fun SocialLinkButton(text: String, url: String, iconRes: Int, context: Context) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { openUrl(context, url) }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = Color.Unspecified // Ensure the icon is displayed with its original colors
            )
        }
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageDialog(imageUrl: String?, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(onClick = onDismiss)
            )
        }
    }
}
