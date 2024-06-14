package es.dam.pi.vinilaapp_v3.ui.login.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import es.dam.pi.vinilaapp_v3.viewmodel.LoginViewModel


/**
 * Pantalla de registro de usuario.
 * @param viewModel ViewModel de la pantalla de registro.
 * @param navController Controlador de navegación.
 * @see LoginViewModel
 * @see NavController
 *
 */
@Composable
fun SignInScreen(viewModel: LoginViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        SignIn(Modifier.align(Alignment.Center), viewModel, navController)
    }
}

@Composable
fun SignIn(modifier: Modifier, viewModel: LoginViewModel, navController: NavController) {
    var email by rememberSaveable { mutableStateOf(viewModel.email.value ?: "") }
    var password by rememberSaveable { mutableStateOf(viewModel.password.value ?: "") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    // Verifica si las contraseñas coinciden
    val passwordsMatch = password == confirmPassword

    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        EmailField(email, { email = it; viewModel.enableDisableLogin(email, password) })
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(password, { password = it; viewModel.enableDisableLogin(email, password) })
        Spacer(modifier = Modifier.height(8.dp))
        ConfirmPasswordField(confirmPassword, { confirmPassword = it })
        Spacer(modifier = Modifier.height(16.dp))
        SignInButton(viewModel.isLoginEnable.value ?: false && passwordsMatch) {
            viewModel.createUserWithEmailAndPassword(email, password, home = {
                navController.navigate("home")
            }, context)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ConfirmPasswordField(confirmPassword: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = confirmPassword,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text(text = "Confirmar Contraseña", modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)  },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        visualTransformation = PasswordVisualTransformation(),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
    )
}

@Composable
fun SignInButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (loginEnable) MaterialTheme.colorScheme.primary else Color.Gray,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = Color.LightGray
        ),
        enabled = loginEnable
    ) {
        Text(text = "Crear cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
