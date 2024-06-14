package es.dam.pi.vinilaapp_v3.ui.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.data.api.ApiService
import es.dam.pi.vinilaapp_v3.viewmodel.PhotosViewModel
import es.dam.pi.vinilaapp_v3.viewmodel.provider.PhotosViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun AboutUsScreen() {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://vinilapp-c328e-default-rtdb.europe-west1.firebasedatabase.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    val viewModelFactory = PhotosViewModelFactory(apiService)
    val photosViewModel: PhotosViewModel = viewModel(factory = viewModelFactory)
    val photos by photosViewModel.photos.collectAsState()

    var isFullscreen by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }

    if (isFullscreen) {
        FullscreenPhotoCarousel(
            photos = photos,
            startIndex = currentIndex,
            onDismiss = { isFullscreen = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Nuestros comienzos",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(resId = R.font.vinilofont)),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            VideoSection(videoId = "ozGGJNo9aNI")
            Spacer(modifier = Modifier.height(16.dp))
            VideoSection(videoId = "1hnQkIMObJE")
            Spacer(modifier = Modifier.height(16.dp))
            DescriptionSection()
            VideoSection(videoId = "XU89eYhNTGE")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Cada fin de semana cientos de postales que dejan con los conciertos!",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(resId = R.font.vinilofont)),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))
            PhotoCarousel(
                photos = photos,
                onPhotoClick = { index ->
                    currentIndex = index
                    isFullscreen = true
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¿Qué aprendimos en pandemia? ¡Que no podemos vivir sin vosotros!",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(resId = R.font.vinilofont2)),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            VideoSection(videoId = "hD4h6QF2lxw")
        }
    }
}

@Composable
fun HeaderSection() {
    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))
    val myFontFamily2 = FontFamily(Font(resId = R.font.vinilofont2))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Quiénes Somos!",
            fontSize = 36.sp,
            fontFamily = myFontFamily2,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.vinilogo),
            contentDescription = "Logo de Los Vinilos",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
fun DescriptionSection() {
    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))
    val myFontFamily2 = FontFamily(Font(resId = R.font.vinilofont2))
    Column {
        Text(
            text = "Historia de Los Vinilos",
            fontSize = 24.sp,
            fontFamily = myFontFamily2,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Los Vinilos es el grupo a través del cual nació COMUNIDAD VINILOS. \n" +
                    "\n" +
                    "Se trata de un grupo tributo a la Edad de Oro del Pop Español, todos los grupos de la movida madrileña y todos los temas de los artistas con los que hemos crecido; Los Secretos, La Guardia, Loquillo, Hombres G, Tino Casal, Burning, El Ultimo de la Fila, Duncan Dhu, Los Ronaldos, Los Rebeldes, Danza Invisible, Alaska, Celtas Cortos, Nacha Pop, entre otros.\n" +
                    "\n" +
                    "Es un grupo con un recorrido espectacular, un verdadero fenómeno que, desde su formación en 2009, ha ofrecido más de un millar de conciertos a lo largo de toda la geografía española.\n" +
                    "\n" +
                    "Los Vinilos se caracterizan por la diversión, la cercanía al público y una fuerza y vitalidad del directo que hace de sus conciertos una experiencia de disfrute que te hará revivir los momentos especiales de tu vida.\n" +
                    "\n" +
                    "Los Vinilos es el corazón de Comunidad Vinilos, realizando cientos de conciertos durante el año. La gran variedad de músicos y las posibilidades de combinaciones entre ellos dotan a Comunidad Vinilos y Los Vinilos de un sello de calidad superior. No te pierdas los cientos de conciertos que vendrán en verano. ¡Aprovecha la oportunidad y disfruta de Los Vinilos siempre que puedas!",
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Justify,
            fontFamily = myFontFamily2,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun PhotoCarousel(photos: List<Any>, onPhotoClick: (Int) -> Unit) {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        HorizontalPager(
            count = photos.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val photo = photos[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onPhotoClick(page) }
            ) {
                if (photo is Int) {
                    Image(
                        painter = painterResource(id = photo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (photo is String) {
                    GlideImage(
                        model = photo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
        IconButton(
            onClick = { onPhotoClick(pagerState.currentPage) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)

        ) {
            Icon(
                imageVector = Icons.Default.Fullscreen,
                contentDescription = "Fullscreen",
                tint = Color.White
            )
        }
    }
}

@Composable
fun VideoSection(videoId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mira este vídeo!",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(resId = R.font.vinilofont)),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        YouTubePlayerView(
            context = LocalContext.current,
            videoId = videoId,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun FullscreenPhotoCarousel(
    photos: List<Any>,
    startIndex: Int,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = startIndex)
    BoxWithConstraints (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
            ) {
        val isLandscape = maxWidth > maxHeight

        HorizontalPager(
            count = photos.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val photo = photos[page]
            if (photo is Int) {
                Image(
                    painter = painterResource(id = photo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .let {
                            if (isLandscape) it else it.padding(vertical = 16.dp)
                        }
                )
            } else if (photo is String) {
                GlideImage(
                    model = photo,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .let {
                            if (isLandscape) it else it.padding(vertical = 16.dp)
                        }
                )
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Color.White,
            inactiveColor = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
        IconButton(
            onClick = { onDismiss() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
    }
}