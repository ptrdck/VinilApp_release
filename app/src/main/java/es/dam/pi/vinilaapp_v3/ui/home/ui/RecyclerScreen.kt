package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import es.dam.pi.vinilaapp_v3.ui.home.provider.PaletaColores
import es.dam.pi.vinilaapp_v3.ui.model.ViniloModel
import es.dam.pi.vinilaapp_v3.ui.home.provider.ViniloProvider
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.ui.theme.RetroWarmBackground

@Composable
fun RecyclerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        TitleText()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(ViniloProvider.listadoVinilos.chunked(2)) { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (item in rowItems) {
                        ItemView(vinilo = item, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ItemView(vinilo: ViniloModel, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        MusicianDetailDialog(vinilo = vinilo, onDismissRequest = { showDialog = false })
    }

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
            .padding(8.dp)
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = vinilo.foto),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = vinilo.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = vinilo.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun MusicianDetailDialog(vinilo: ViniloModel, onDismissRequest: () -> Unit) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = vinilo.foto),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = vinilo.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = vinilo.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = vinilo.biografia,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ClickableText(
                    text = AnnotatedString(
                        text = "Para más información consulta: ${vinilo.web}",
                        spanStyles = listOf(
                            AnnotatedString.Range(
                                SpanStyle(color = MaterialTheme.colorScheme.primary),
                                start = 27,
                                end = 27 + vinilo.web.length
                            )
                        )
                    ),
                    onClick = { offset ->
                        if (offset in 27..(27 + vinilo.web.length)) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vinilo.web))
                            context.startActivity(intent)
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismissRequest, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("Cerrar")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TitleText() {
    val primaryFontFamily = FontFamily(Font(resId = R.font.vinilofont))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFFFF8E1)) // Fondo claro para coherencia
            .border(BorderStroke(1.dp, Color(0xFFC70039))) // Borde rojo
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuestros",
                style = TextStyle(
                    fontFamily = primaryFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFC70039), // Rojo
                            Color(0xFFFF5733), // Naranja
                            Color(0xFFFFC300)  // Amarillo
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1000f)
                    ),
                    shadow = Shadow(color = Color.Gray, offset = Offset(3f, 3f), blurRadius = 6f) // Sombra más pronunciada para efecto retro
                ),
                textAlign = TextAlign.Center
            )
            // Five Stars Aligned Horizontally
            Row(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = Color(0xFFFFC300),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Text(
                text = "Músicos Vinilos",
                style = TextStyle(
                    fontFamily = primaryFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color(0xFF000000), // Color negro para texto
                    shadow = Shadow(color = Color.Gray, offset = Offset(3f, 3f), blurRadius = 6f) // Sombra más pronunciada
                ),
                textAlign = TextAlign.Center
            )
            // Línea decorativa para efecto ochentero
            Divider(
                color = Color(0xFFC70039),
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 8.dp).width(150.dp)
            )
        }
    }
}
