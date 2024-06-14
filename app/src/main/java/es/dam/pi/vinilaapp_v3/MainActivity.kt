package es.dam.pi.vinilaapp_v3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.dam.pi.vinilaapp_v3.ui.home.ui.LoadingScreen
import es.dam.pi.vinilaapp_v3.ui.login.ui.GoogleAuthUiClient
import es.dam.pi.vinilaapp_v3.ui.login.ui.LoginScreen
import es.dam.pi.vinilaapp_v3.ui.login.ui.SignInScreen
import es.dam.pi.vinilaapp_v3.ui.theme.LightOnlyTheme
import es.dam.pi.vinilaapp_v3.viewmodel.LoginViewModel
import es.dam.pi.vinilaapp_v3.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val googleAuthUiClient = GoogleAuthUiClient(this, googleSignInClient)
        val database = FirebaseDatabase.getInstance()
        val userViewModel = UserViewModel(database)
        val loginViewModel = LoginViewModel(googleAuthUiClient, userViewModel)

        setContent {
            LightOnlyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val (loadingFinished, setLoadingFinished) = remember { mutableStateOf(false) }

                    LaunchedEffect(loadingFinished) {
                        if (loadingFinished) {
                            val currentUser = Firebase.auth.currentUser
                            if (currentUser != null) {
                                // User is signed in, navigate to HomeActivity
                                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                finish()
                            } else {
                                navController.navigate("login")
                            }
                        }
                    }

                    if (!loadingFinished) {
                        LoadingScreen(onTimeout = { setLoadingFinished(true) })
                    } else {
                        NavHost(navController = navController, startDestination = "login") {
                            composable("login") {
                                LoginScreen(loginViewModel, navController)
                            }
                            composable("signUp") {
                                SignInScreen(loginViewModel, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
