package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.app.DatePickerDialog
import android.content.Context
import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.ui.model.Contract
import es.dam.pi.vinilaapp_v3.viewmodel.ContractViewModel
import es.dam.pi.vinilaapp_v3.viewmodel.LocationViewModel
import java.util.Calendar

@Composable
fun ContractFormScreen(
    navController: NavController,
    contractViewModel: ContractViewModel,
    locationViewModel: LocationViewModel
) {
    val contract by contractViewModel.contract.collectAsState()
    val isFormValid by contractViewModel.isFormValid.collectAsState()
    val errorState by contractViewModel.errorState.collectAsState()
    val successState by contractViewModel.successState.collectAsState()
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            contract = contract,
            onConfirm = {
                showConfirmationDialog = false
                contractViewModel.createContract()
                showSuccessDialog = true
            },
            onDismiss = { showConfirmationDialog = false }
        )
    }
    if (showSuccessDialog) {
        SuccessDialog(
            message = successState ?: "Datos enviados con éxito",
            onDismiss = {
                showSuccessDialog = false
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
                contractViewModel.clearMessages()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            CustomText()
            Spacer(modifier = Modifier.height(16.dp))
            ContactSection(
                name = contract.name,
                email = contract.email,
                phone = contract.phone,
                onNameChange = { contractViewModel.updateField(name = it) },
                onEmailChange = { contractViewModel.updateField(email = it) },
                onPhoneChange = { contractViewModel.updateField(phone = it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ConcertTypeSection(
                concertType = contract.concertType,
                onConcertTypeChange = { contractViewModel.updateField(concertType = it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DurationDropdown(
                duration = contract.duration,
                onDurationChange = { contractViewModel.updateField(duration = it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ConcertDateField(
                concertDate = contract.concertDate,
                onConcertDateChange = { contractViewModel.updateField(concertDate = it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AddressInputField(
                contractViewModel = contractViewModel,
                context = LocalContext.current
            )
            Spacer(modifier = Modifier.height(16.dp))
            DescriptionInputField(
                description = contract.description,
                onDescriptionChange = { contractViewModel.updateField(description = it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!isFormValid) {
                Text(
                    text = "Todos los campos son obligatorios y deben estar correctamente llenos",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ButtonSection(
                onSendClick = { showConfirmationDialog = true },
                onClearClick = {
                    contractViewModel.updateField(
                        name = "", email = "", phone = "", concertType = "privado", duration = "2h",
                        description = "", concertDate = "", address = "", latitude = null, longitude = null
                    )
                },
                onCancelClick = { navController.navigate("home") }
            )
        }
    }

    errorState?.let {
        Snackbar(
            action = {
                Button(onClick = { contractViewModel.clearMessages() }) {
                    Text("Cerrar")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = it)
        }
    }

    successState?.let {
        Snackbar(
            action = {
                Button(onClick = { contractViewModel.clearMessages() }) {
                    Text("Cerrar")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = it)
        }
    }
}

@Composable
fun AddressInputField(
    contractViewModel: ContractViewModel,
    context: Context
) {
    var address by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.g_maps_id))
        }
    }

    val placesClient: PlacesClient = Places.createClient(context)

    Column(modifier = Modifier.padding(8.dp)) {
        TextField(
            value = address,
            onValueChange = {
                address = it
                if (it.isNotEmpty()) {
                    val request = com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest.builder()
                        .setQuery(it)
                        .build()
                    placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                        predictions = response.autocompletePredictions
                    }.addOnFailureListener { exception ->
                        predictions = emptyList()
                        exception.printStackTrace()
                    }
                } else {
                    predictions = emptyList()
                }
            },
            label = { Text("Dirección") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        predictions.forEach { prediction ->
            DropdownMenuItem(
                text = { Text(prediction.getFullText(null).toString()) },
                onClick = {
                    address = prediction.getFullText(null).toString()
                    contractViewModel.updateField(address = address)
                    predictions = emptyList()
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AddressMapPicker(
            addressText = address,
            onAddressSelected = { selectedAddress, latitude, longitude ->
                address = selectedAddress
                contractViewModel.updateField(
                    address = selectedAddress,
                    latitude = latitude,
                    longitude = longitude
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationDropdown(duration: String, onDurationChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val durations = listOf("2h", "3h", "4h", "5h")
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(8.dp)
    ) {
        TextField(
            value = duration,
            onValueChange = {},
            readOnly = true,
            label = { Text("Duración") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {            durations.forEach { durationOption ->
            DropdownMenuItem(
                text = { Text(durationOption) },
                onClick = {
                    onDurationChange(durationOption)
                    expanded = false
                }
            )
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcertDateField(concertDate: String, onConcertDateChange: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val date = remember { mutableStateOf(concertDate) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            date.value = selectedDate
            onConcertDateChange(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Fecha del concierto", style = MaterialTheme.typography.labelLarge)
        TextField(
            value = date.value,
            onValueChange = { date.value = it },
            label = { Text("Fecha del concierto") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Seleccionar fecha",
                    modifier = Modifier.clickable { datePickerDialog.show() }
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
fun DescriptionInputField(description: String, onDescriptionChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Descripción del concierto", style = MaterialTheme.typography.labelLarge)
        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Agregue información relevante del concierto, cumpleaños, boda, comunión, hora, indicaciones, etc.") },
            singleLine = false,
            maxLines = 4,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
fun ConfirmationDialog(contract: Contract, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Confirmar Envío") },
        text = {
            Text(
                "Nombre: ${contract.name}\n" +
                        "Correo: ${contract.email}\n" +
                        "Teléfono: ${contract.phone}\n" +
                        "Tipo de concierto: ${contract.concertType}\n" +
                        "Duración: ${contract.duration}\n" +
                        "Fecha del concierto: ${contract.concertDate}\n" +
                        "Dirección: ${contract.address}\n" +
                        "Descripción: ${contract.description}"
            )
        }
    )
}

@Composable
fun SuccessDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        },
        title = { Text("Envío Exitoso") },
        text = { Text(message) }
    )
}

@Composable
fun ContactSection(
    name: String,
    email: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    val currentUser = Firebase.auth.currentUser
    var userName by remember { mutableStateOf(currentUser?.displayName ?: name) }
    var userEmail by remember { mutableStateOf(currentUser?.email ?: email) }
    var userPhone by remember { mutableStateOf(phone) }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Contacto", style = MaterialTheme.typography.labelLarge)
        TextField(
            value = userName,
            onValueChange = {
                userName = it
                onNameChange(it)
            },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = userEmail,
            onValueChange = {
                userEmail = it
                onEmailChange(it)
            },
            label = { Text("Correo") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            isError = !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )
        TextField(
            value = userPhone,
            onValueChange = {
                userPhone = it
                onPhoneChange(it)
            },
            label = { Text("Teléfono") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            isError = !Patterns.PHONE.matcher(userPhone).matches(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun ConcertTypeSection(concertType: String, onConcertTypeChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Tipo de concierto", style = MaterialTheme.typography.labelLarge)
        Row {
            RadioButton(
                selected = concertType == "privado",
                onClick = { onConcertTypeChange("privado") }
            )
            Text(text = "Privado")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = concertType == "público",
                onClick = { onConcertTypeChange("público") }
            )
            Text(text = "Público")
        }
    }
}

@Composable
fun ButtonSection(onSendClick: () -> Unit, onClearClick: () -> Unit, onCancelClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onSendClick, modifier = Modifier.size(48.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.Green)
                }
                Text(text = "enviar", color = Color.Green, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onClearClick, modifier = Modifier.size(48.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Limpiar", tint = Color.Red)
                }
                Text(text = "limpiar", color = Color.Red, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onCancelClick, modifier = Modifier.size(48.dp)) {
                    Icon(imageVector = Icons.Default.Cancel, contentDescription = "Cancelar", tint = Color.Gray)
                }
                Text(text = "cancelar", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CustomText() {
    val viniloFontFamily = FontFamily(Font(R.font.vinilofont))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Alinear el texto horizontalmente
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFFFF8E1), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
                .border(BorderStroke(2.dp, Color(0xFFC70039)), shape = RoundedCornerShape(8.dp))
                .shadow(8.dp, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿Quieres",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC70039), // Color más oscuro para mejor contraste
                    fontFamily = viniloFontFamily, // Aplicar la fuente personalizada
                    modifier = Modifier
                        .shadow(2.dp, shape = RoundedCornerShape(4.dp)) // Sombra más sutil para mejor visibilidad
                        .background(Color(0xFFFFF8E1), shape = RoundedCornerShape(4.dp)) // Fondo sutil
                        .padding(horizontal = 8.dp, vertical = 4.dp) // Espaciado alrededor del texto
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Contratarnos?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC70039), // Color más oscuro para mejor contraste
                    fontFamily = viniloFontFamily, // Aplicar la fuente personalizada
                    modifier = Modifier
                        .shadow(2.dp, shape = RoundedCornerShape(4.dp)) // Sombra más sutil para mejor visibilidad
                        .background(Color(0xFFFFF8E1), shape = RoundedCornerShape(4.dp)) // Fondo sutil
                        .padding(horizontal = 8.dp, vertical = 4.dp) // Espaciado alrededor del texto
                )
            }
        }
    }
}


@Composable
fun PreviewCustomText() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1)), // Fondo claro para coherencia
        contentAlignment = Alignment.Center
    ) {
        CustomText()
    }
}


