package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.widget.ImageView
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.viewmodel.ThemeViewModel
import es.dam.pi.vinilaapp_v3.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel = viewModel(), themeViewModel: ThemeViewModel = viewModel()) {
    val user = Firebase.auth.currentUser
    val userId = Firebase.auth.currentUser?.uid
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    // Cargar los datos del usuario
    LaunchedEffect(userId) {
        if (userId != null) {
            userViewModel.loadUser(userId, user)
        }
    }

    val userState by userViewModel.user.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()

    val username = userState?.username ?: "Usuario"
    val profilePictureUrl = userState?.profilePictureUrl

    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))

    if (isLoading) {
        LoadingScreen()
    } else {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            val screenWidth = constraints.maxWidth

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vinilapp_logo),
                    contentDescription = "Vinilogo",
                    modifier = Modifier
                        .size(if (screenWidth > 600) 200.dp else 150.dp)
                )
                AnimatedGradientText(
                    text = "Bienvenido $username",
                    fontFamily = myFontFamily,
                    fontSize = if (screenWidth > 600) 30.sp else 24.sp,
                    colors = listOf(Color(0xFFFFA726), Color(0xFFFF4500), Color(0xFF8B0000), Color(0xFF000000))
                )
                Spacer(modifier = Modifier.height(12.dp))
                UserImage(navController, profilePictureUrl)
                Spacer(modifier = Modifier.height(12.dp))
                ThemeSwitch(isDarkTheme = isDarkTheme, onToggle = { themeViewModel.toggleTheme() })
                Spacer(modifier = Modifier.height(6.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        NavigationIcon(
                            imageResource = R.drawable.aboutus,
                            contentDescription = "¿Quienes Somos?",
                            onClick = { navController.navigate("aboutUsScreen") },
                            label = "¿Quienes Somos?"
                        )
                    }
                    item {
                        NavigationIcon(
                            imageResource = R.drawable.recyclerview,
                            contentDescription = "Plantilla Vinila",
                            onClick = { navController.navigate("recyclerScreen") },
                            label = "Plantilla Vinila"
                        )
                    }
                    item {
                        NavigationIcon(
                            imageResource = R.drawable.usergallery,
                            contentDescription = "Galería de Usuario",
                            onClick = { navController.navigate("gallery") },
                            label = "Galería de Usuario"
                        )
                    }
                    item {
                        NavigationIcon(
                            imageResource = R.drawable.contractform,
                            contentDescription = "Formulario de Contrato",
                            onClick = { navController.navigate("contractFormScreen") },
                            label = "Formulario de Contrato"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserImage(navController: NavController, profilePictureUrl: String?) {
    AndroidView({ context ->
        ImageView(context).apply {
            if (profilePictureUrl != null) {
                Glide.with(context).load(profilePictureUrl).placeholder(R.drawable.avatar).into(this)
            } else {
                this.setImageResource(R.drawable.avatar)
            }
        }
    }, modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .clickable { navController.navigate("userScreen") }
    )
}

@Composable
fun NavigationIcon(imageResource: Int, contentDescription: String, onClick: () -> Unit, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(100.dp)
                .clickable { onClick() }
                .padding(8.dp)
        )
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(resId = R.font.vinilofont)),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ThemeSwitch(isDarkTheme: Boolean, onToggle: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFFFF8E1),
                uncheckedThumbColor = Color.Transparent,
                checkedTrackColor = Color(0xFFFFC300),
                uncheckedTrackColor = Color.LightGray,
                disabledCheckedThumbColor = Color(0xFFFFF8E1),
                disabledUncheckedThumbColor = Color.Transparent,
                disabledCheckedTrackColor = Color(0xFFFFC300),
                disabledUncheckedTrackColor = Color.LightGray
            ),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = if (isDarkTheme) "Modo Oscuro" else "Modo Claro",
            fontSize = 8.sp,
            color = if (isDarkTheme) Color(0xFFFFF8E1) else Color.Black,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

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
fun GradientText(text: String, fontFamily: FontFamily, fontSize: TextUnit, colors: List<Color>, shadowColor: Color) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize,
            fontFamily = fontFamily,
            brush = Brush.radialGradient(
                colors = colors,
                center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                radius = 900f
            ),
            shadow = Shadow(
                color = shadowColor,
                blurRadius = 8f
            )
        ),
        textAlign = TextAlign.Center
    )
}
