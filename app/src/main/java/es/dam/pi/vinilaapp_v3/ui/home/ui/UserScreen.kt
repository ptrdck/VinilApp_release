package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.ui.model.Contract
import es.dam.pi.vinilaapp_v3.ui.model.UserData
import es.dam.pi.vinilaapp_v3.utils.NetworkUtils.toUri
import es.dam.pi.vinilaapp_v3.viewmodel.ContractViewModel
import es.dam.pi.vinilaapp_v3.viewmodel.UserViewModel


@Composable
fun UserScreen(
    userViewModel: UserViewModel = viewModel(),
    contractViewModel: ContractViewModel = viewModel(),
    navController: NavController
) {
    val userId = Firebase.auth.currentUser?.uid

    if (userId != null) {
        LaunchedEffect(userId) {
            userViewModel.loadUser(userId)
            contractViewModel.loadContracts(userId)
        }
    }

    val userState by userViewModel.user.collectAsState()
    val contracts by contractViewModel.contracts.collectAsState()
    val imageUploadSuccess by userViewModel.imageUploadSuccess.collectAsState()
    val tempProfilePictureUrl by userViewModel.tempProfilePictureUrl.collectAsState()
    var username by remember { mutableStateOf(userState?.username ?: "") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))

    LaunchedEffect(imageUploadSuccess) {
        if (imageUploadSuccess) {
            userViewModel.resetImageUploadSuccess()
            userViewModel.loadUser(userId!!)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Espacio",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = myFontFamily,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfilePicture(tempProfilePictureUrl, userState?.profilePictureUrl, Modifier.size(120.dp), userViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        UsernameField(username = username, onValueChange = { username = it })
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                SaveButton(onClick = {
                    if (username.isNotBlank()) {
                        userState?.let { user ->
                            val updatedUser = user.copy(username = username)
                            userViewModel.updateUser(updatedUser)
                            userViewModel.saveProfilePicture()
                            showSuccessMessage = true
                            navController.navigate("home")
                        }
                    } else {
                        showErrorMessage = true
                        errorMessage = "El nombre de usuario no puede estar vacío."
                    }
                })
            }
            Box(
                modifier = Modifier.weight(1f)
            ) {
                CancelButton(onClick = {
                    userViewModel.cancelProfilePictureUpdate()
                    navController.navigate("home")
                })
            }
        }
        if (showSuccessMessage) {
            Text(
                text = "¡Perfil actualizado con éxito!",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        if (showErrorMessage) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Mis Contratos",
            fontSize = 20.sp,
            fontFamily = myFontFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(contracts) { contract ->
                ContractItem(contract = contract)
            }
        }
    }
}

@Composable
fun ProfilePicture(
    tempProfilePictureUrl: String?,
    currentProfilePictureUrl: String?,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            val uri = it.toUri(context)
            userViewModel.setTempProfilePictureUrl(uri.toString())
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            // Mostrar un mensaje de que el permiso no fue otorgado
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            userViewModel.setTempProfilePictureUrl(it.toString())
        }
    }

    Box {
        AndroidView({ ctx ->
            ImageView(ctx).apply {
                val loadUrl = tempProfilePictureUrl ?: currentProfilePictureUrl ?: R.drawable.avatar
                Glide.with(context).load(loadUrl).placeholder(R.drawable.avatar).into(this)
            }
        }, modifier = modifier
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable { showDialog = true }
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Seleccione una opción") },
                text = {
                    Column {
                        Button(onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            } else {
                                takePictureLauncher.launch(null)
                            }
                            showDialog = false
                        }) {
                            Text("Tomar foto")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            pickImageLauncher.launch("image/*")
                            showDialog = false
                        }) {
                            Text("Seleccionar de la galería")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            userViewModel.clearTempProfilePictureUrl()
                            showDialog = false
                        }) {
                            Text("Eliminar foto actual")
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}


@Composable
fun ContractItem(contract: Contract) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize()
        ) {
            Text(text = contract.address, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = contract.concertType, color = MaterialTheme.colorScheme.onSurface)
            Text(text = contract.concertDate, color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = contract.status,
                color = if (contract.status == "Solicitado") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            if (isExpanded) {
                Text(text = "Dirección: ${contract.address}", color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Tipo de concierto: ${contract.concertType}", color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Duración: ${contract.duration}", color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Fecha del concierto: ${contract.concertDate}", color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Descripción: ${contract.description}", color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Teléfono: ${contract.phone}", color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Correo: ${contract.email}", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun UsernameField(username: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = username,
        onValueChange = onValueChange,
        label = { Text("Nombre de usuario") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun SaveButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun CancelButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary)
    }
}
