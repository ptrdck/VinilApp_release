package es.dam.pi.vinilaapp_v3.ui.login.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import es.dam.pi.vinilaapp_v3.HomeActivity
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.ui.theme.VinilaAppTheme
import es.dam.pi.vinilaapp_v3.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    //aplicamos Themestatus

    val loginSuccess by viewModel.loginSuccess.collectAsState(initial = false)
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                viewModel.handleGoogleSignInResult(data, { signInResult ->
                    if (signInResult.errorMessage != null) {
                        showToast(context, signInResult.errorMessage)
                    } else {
                        // Crear un Intent para HomeActivity
                        val intent = Intent(context, HomeActivity::class.java)
                        // Iniciar HomeActivity
                        context.startActivity(intent)
                    }
                }, {
                    showToast(context, "Error al iniciar sesión con Google")
                })
            } ?: showToast(context, "Error al obtener los datos del inicio de sesión con Google")
        } else {
            showToast(context, "Error al iniciar sesión con Google")
        }
    }

    if (loginSuccess) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel, navController, googleSignInLauncher)
    }
}
@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController, googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    var email by rememberSaveable { mutableStateOf(viewModel.email.value ?: "") }
    var password by rememberSaveable { mutableStateOf(viewModel.password.value ?: "") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.weight(2f))
        EmailField(email, { email = it; viewModel.enableDisableLogin(email, password) })
        Spacer(modifier = Modifier.weight(1f))
        PasswordField(password, { password = it; viewModel.enableDisableLogin(email, password) })
        Spacer(modifier = Modifier.weight(1f))
        ForgotPassword()
        Spacer(modifier = Modifier.weight(1f))
        LoginButton(viewModel.isLoginEnable.value ?: false) {
            viewModel.signInWithEmailAndPassword(email, password, context)
        }
        Spacer(modifier = Modifier.weight(1f))
        SignUpOption {
            navController.navigate("signUp")
        }
        Spacer(modifier = Modifier.weight(1f))
        GoogleLoginOption(viewModel, googleSignInLauncher)
        Spacer(modifier = Modifier.weight(3f))
        Text(
            text = "Diseño y desarrollo por @ptrdck",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.vinilofont)),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}




@Composable
fun PasswordField(password: String, onTextFielChanged:(String) -> Unit ){
    TextField(
        value = password,
        onValueChange = { onTextFielChanged(it) },
        placeholder = { Text(text = "Contraseña",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        visualTransformation = PasswordVisualTransformation(),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)

    )
}


@Composable
fun HeaderImage(modifier: Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            ImageView(context).apply {
                Glide.with(context)
                    .asGif()
                    .load(R.drawable.vinilap_logo)  // Asegúrate de que tu GIF se llama header_animation.gif
                    .into(this)
            }
        }
    )
}

@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text(text = "Correo Electrónico",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
    )
}

@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (loginEnable) MaterialTheme.colorScheme.primary else Color.Gray,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = Color.LightGray
        ),
        enabled = loginEnable
    ) {
        Text(text = "Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GoogleLoginOption(viewModel: LoginViewModel, googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val context = LocalContext.current
    val email = viewModel.email.value ?: ""
    val userName = email.substringBefore("@")

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .height(48.dp)
                .clickable {
                    viewModel.signInWithGoogle({ intent ->
                        googleSignInLauncher.launch(intent)
                    }, {
                        showToast(context, "Error al iniciar sesión con Google")
                    })
                }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Inicia sesión con Google", fontSize = 14.sp)
        }
    }
}

@Composable
fun SignUpOption(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "¿No tienes cuenta? Crea una ya!",
            modifier = Modifier
                .clickable { onClick() }
                .align(Alignment.Center)
                .padding(4.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
fun ForgotPassword() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Olvidaste tu contraseña?",
            modifier = Modifier
                .clickable { /* Acción de recuperación de contraseña */ }
                .align(Alignment.Center)
                .padding(4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3366BB) // Un color azul distintivo
        )
    }
}



fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
