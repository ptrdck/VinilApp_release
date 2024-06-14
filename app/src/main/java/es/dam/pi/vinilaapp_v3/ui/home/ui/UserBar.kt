package es.dam.pi.vinilaapp_v3.ui.home.ui

import android.content.Context
import android.content.Intent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.dam.pi.vinilaapp_v3.MainActivity
import es.dam.pi.vinilaapp_v3.R

@Composable
fun BottomNavigationBar(navController: NavController, context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    val myFontFamily = FontFamily(Font(resId = R.font.vinilofont))
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("userScreen", Icons.Default.Person, "User"),
        BottomNavItem("news", Icons.Filled.NewReleases, "Noticias"),
        BottomNavItem("logout", Icons.AutoMirrored.Filled.ExitToApp, "Logout")
    )

    if (showDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                Firebase.auth.signOut()
                context.startActivity(Intent(context, MainActivity::class.java))
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.label, tint = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground) },
                label = { Text(item.label, fontFamily = myFontFamily, color = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground) },
                selected = currentRoute == item.route,
                onClick = {
                    when (item.route) {
                        "logout" -> {
                            showDialog = true
                        }
                        "home" -> {
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                        else -> navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmación", color = MaterialTheme.colorScheme.onBackground) },
        text = { Text("¿Estás seguro de que quieres salir?", color = MaterialTheme.colorScheme.onBackground) },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text("Sí", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                Text("No", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    )
}

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)
