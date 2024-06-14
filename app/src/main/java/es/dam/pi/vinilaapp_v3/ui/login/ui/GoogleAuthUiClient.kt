package es.dam.pi.vinilaapp_v3.ui.login.ui

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInClient

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import es.dam.pi.vinilaapp_v3.ui.model.SignInResult
import es.dam.pi.vinilaapp_v3.ui.model.UserData
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException



/**
* Clase que se encarga de gestionar la autenticación con Google.
* @param context Contexto de la aplicación.
* @param googleSignInClient Cliente de inicio de sesión con Google.
 * @property context
 * @property googleSignInClient
 * @property auth
 * @constructor
 * @param context
 * @param googleSignInClient
 *
 */
class GoogleAuthUiClient(
    private val context: Context,
    private val googleSignInClient: GoogleSignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): Intent {
        return googleSignInClient.signInIntent
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        return try {
            val account = task.getResult(ApiException::class.java)
            val googleIdToken = account?.idToken
            if (googleIdToken != null) {
                val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                try {
                    val result = auth.signInWithCredential(googleCredentials).await()
                    val user = result.user
                    if (user != null) {
                        // El inicio de sesión en Firebase fue exitoso, devolver los datos del usuario
                        SignInResult(
                            data = UserData(
                                userId = user.uid,
                                username = user.displayName ?: "", // Manejar nulos
                                profilePictureUrl = user.photoUrl?.toString() ?: "" // Manejar nulos
                            ),
                            errorMessage = null
                        )
                    } else {
                        // El inicio de sesión en Firebase falló, devolver un error
                        SignInResult(
                            data = null,
                            errorMessage = "Error al iniciar sesión en Firebase"
                        )
                    }
                } catch (e: Exception) {
                    // Hubo un error al iniciar sesión en Firebase, devolver un error
                    SignInResult(
                        data = null,
                        errorMessage = e.message
                    )
                }
            } else {
                // googleIdToken es nulo
                SignInResult(
                    data = null,
                    errorMessage = "Google ID token is null"
                )
            }
        } catch(e: ApiException) {
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            googleSignInClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName ?: "", // Manejar nulos
            profilePictureUrl = photoUrl?.toString() ?: "" // Manejar nulos
        )
    }
}
