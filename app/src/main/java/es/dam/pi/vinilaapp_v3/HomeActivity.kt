package es.dam.pi.vinilaapp_v3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.dam.pi.vinilaapp_v3.data.ContractDatabaseManager
import es.dam.pi.vinilaapp_v3.ui.home.ui.*
import es.dam.pi.vinilaapp_v3.ui.login.ui.GoogleAuthUiClient
import es.dam.pi.vinilaapp_v3.ui.theme.VinilaAppTheme
import es.dam.pi.vinilaapp_v3.viewmodel.*
import es.dam.pi.vinilaapp_v3.viewmodel.provider.ContractViewModelFactory
import es.dam.pi.vinilaapp_v3.viewmodel.provider.LoginViewModelFactory
import es.dam.pi.vinilaapp_v3.viewmodel.provider.UserViewModelFactory

class HomeActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(FirebaseDatabase.getInstance())
    }

    private val googleAuthUiClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        GoogleAuthUiClient(applicationContext, googleSignInClient)
    }

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(googleAuthUiClient, userViewModel)
    }

    private lateinit var placesClient: PlacesClient

    private val locationViewModel: LocationViewModel by viewModels()

    private val userGalleryViewModel: UserGalleryViewModel by viewModels()

    private val newsViewModel: NewsViewModel by viewModels() // Añadido para el NewsViewModel

    private val themeViewModel: ThemeViewModel by viewModels() // Añadido para el ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Places
        Places.initialize(applicationContext, getString(R.string.g_maps_id))
        placesClient = Places.createClient(this)
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            VinilaAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFA500)) // Fondo naranja
                ) {
                    val navController = rememberNavController()
                    val contractDatabaseManager = ContractDatabaseManager()
                    val contractViewModel: ContractViewModel = ViewModelProvider(
                        this,
                        ContractViewModelFactory(contractDatabaseManager, applicationContext)
                    )[ContractViewModel::class.java]

                    LaunchedEffect(Unit) {
                        locationViewModel.getCurrentLocation(this@HomeActivity)
                    }

                    Scaffold(
                        bottomBar = { BottomNavigationBar(navController, this@HomeActivity) }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("home") {
                                HomeScreen(navController, userViewModel, themeViewModel)
                            }
                            composable("userScreen") {
                                UserScreen(userViewModel, contractViewModel, navController)
                            }
                            composable("recyclerScreen") {
                                RecyclerScreen()
                            }
                            composable("contractFormScreen") {
                                ContractFormScreen(navController, contractViewModel, locationViewModel)
                            }
                            composable("aboutUsScreen") {
                                AboutUsScreen()
                            }
                            composable("gallery") {
                                UserGalleryScreen(viewModel = userGalleryViewModel)
                            }
                            composable("news") {
                                NewsScreen(viewModel = newsViewModel, isDarkTheme = isDarkTheme) // Nueva ruta para Novedades
                            }

                        }
                    }
                }
            }
        }
    }
}
