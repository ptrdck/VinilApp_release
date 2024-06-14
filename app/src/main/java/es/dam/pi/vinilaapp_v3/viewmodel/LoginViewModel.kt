package es.dam.pi.vinilaapp_v3.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.dam.pi.vinilaapp_v3.HomeActivity
import es.dam.pi.vinilaapp_v3.ui.login.ui.GoogleAuthUiClient
import es.dam.pi.vinilaapp_v3.ui.model.SignInResult
import es.dam.pi.vinilaapp_v3.ui.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isPassVisible = MutableLiveData<Boolean>()
    val isPassVisible: LiveData<Boolean> = _isPassVisible

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    fun cambiarVisibilidad(p: Boolean) {
        _isPassVisible.value = !p
    }

    fun enableDisableLogin(email: String, pass: String) {
        _email.value = email
        _password.value = pass
        _isLoginEnable.value =
            !(pass.length < 5 || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    fun signInWithGoogle(onSuccess: (Intent) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val intent = googleAuthUiClient.signIn()
                intent?.let {
                    onSuccess(it)
                } ?: onFailure(Exception("Google Sign In failed."))
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, context: Context) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            if (user.isEmailVerified) {
                                userViewModel.loadUser(user.uid, user)
                                _loginSuccess.value = true
                                val intent = Intent(context, HomeActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "Por favor, verifica tu correo electrónico antes de iniciar sesión.", Toast.LENGTH_LONG).show()
                                auth.signOut()
                            }
                        } else {
                            Toast.makeText(context, "Error al iniciar sesión: Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (ex: Exception) {
            Toast.makeText(context, "Error al iniciar sesión: ${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit, context: Context) = viewModelScope.launch {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            user.sendEmailVerification()
                                .addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        Toast.makeText(context, "Correo de verificación enviado. Por favor, verifica tu correo electrónico.", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "Error al enviar el correo de verificación: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(context, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (ex: Exception) {
            Toast.makeText(context, "Error al registrar: ${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleGoogleSignInResult(
        intent: Intent,
        onSuccess: (SignInResult) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val signInResult = googleAuthUiClient.signInWithIntent(intent)
                if (signInResult.errorMessage != null) {
                    onFailure(Exception(signInResult.errorMessage))
                } else {
                    onSuccess(signInResult)
                    signInResult.data?.let {
                        userViewModel.createUser(it)
                    }
                }
            } catch (ex: Exception) {
                val errorMessage = handleException(ex)
                Log.d("Login", "signInWithEmailAndPassword: $errorMessage")
            }
        }
    }

    fun handleException(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> "Las credenciales de autenticación proporcionadas son incorrectas, están mal formadas o han expirado."
            is FirebaseAuthUserCollisionException -> "El email ingresado ya está en uso por otra cuenta."
            else -> "Ocurrió un error desconocido: ${e.message}"
        }
    }

    fun onLoginFailure(exception: Exception) {
        Log.d("Login", "signInWithEmailAndPassword: ${exception.message}")
    }

    fun signOut() {
        auth.signOut()
        userViewModel.clearUser()
    }
}
