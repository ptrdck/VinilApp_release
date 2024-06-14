package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.ui.model.Image
import es.dam.pi.vinilaapp_v3.viewmodel.UserGalleryViewModel
import java.io.File

@Composable
fun UserGalleryScreen(viewModel: UserGalleryViewModel = viewModel()) {

    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))
    val myFontFamily2 =FontFamily(Font(resId = R.font.vinilofont2))
    val photos by viewModel.photos.collectAsState()
    val context = LocalContext.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPhotos by remember { mutableStateOf(setOf<String>()) }
    var selectionMode by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var currentPhotoIndex by remember { mutableStateOf(0) }
    val maxPhotos = 15

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.uploadPhoto(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            tempUri?.let { viewModel.uploadPhoto(it) }
        } else {
            viewModel.onPhotoTakingFailed()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mi Galería",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = myFontFamily2,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    enabled = photos.size < maxPhotos
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Subir desde Galería",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        tempUri = createTempUri(context)
                        tempUri?.let { cameraLauncher.launch(it) }
                    },
                    enabled = photos.size < maxPhotos
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Tomar Foto",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }


        Text(
            text = "En esta galería procura guardar tus selfies con los músicos de Los Vinilos, antes o después de los concierto! Si juntas muchos diferentes puede que encuentres sorpresas!!!",
            fontFamily = myFontFamily2,
            color = MaterialTheme.colorScheme.secondary, // Set to primary color
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (photos.isEmpty()) {
            EmptyGalleryState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f)
            ) {
                items(photos) { photo ->
                    PhotoItem(
                        photo = photo,
                        isSelected = selectedPhotos.contains(photo.imageId),
                        onPhotoClick = {
                            if (selectionMode) {
                                selectedPhotos = if (selectedPhotos.contains(photo.imageId)) {
                                    selectedPhotos - photo.imageId
                                } else {
                                    selectedPhotos + photo.imageId
                                }
                            } else {
                                currentPhotoIndex = photos.indexOf(photo)
                                showDialog = true
                            }
                        },
                        onLongClick = {
                            selectionMode = true
                            selectedPhotos = setOf(photo.imageId)
                        }
                    )
                }
            }
        }

        if (photos.size >= maxPhotos) {
            Text(
                text = "Has alcanzado la capacidad máxima de almacenamiento.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (selectionMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        viewModel.deleteSelectedPhotos(selectedPhotos.toList())
                        selectedPhotos = emptySet()
                        selectionMode = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Borrar Selección", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = {
                        selectedPhotos = emptySet()
                        selectionMode = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }

    if (showDialog) {
        ImageCarouselDialog(
            images = photos,
            startIndex = currentPhotoIndex,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun EmptyGalleryState() {
    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Galería Vacía",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tu galería está vacía. ¡Sube tus fotos favoritas!",
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = myFontFamily)
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PhotoItem(photo: Image, isSelected: Boolean, onPhotoClick: () -> Unit, onLongClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = onPhotoClick,
                onLongClick = {
                    onLongClick()
                    true
                }
            )
            .border(2.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        GlideImage(
            model = photo.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCarouselDialog(
    images: List<Image>,
    startIndex: Int,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(startIndex) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            GlideImage(
                model = images[currentIndex].imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
            IconButton(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous", tint = Color.White)
            }
            IconButton(
                onClick = {
                    if (currentIndex < images.size - 1) {
                        currentIndex++
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = Color.White)
            }
        }
    }
}

fun createTempUri(context: Context): Uri {
    val tempFile = File.createTempFile("temp_image",".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}
