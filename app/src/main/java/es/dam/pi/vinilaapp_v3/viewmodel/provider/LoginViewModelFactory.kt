// Actualiza la clase LoginViewModelFactory para recibir GoogleAuthUiClient y UserViewModel

package es.dam.pi.vinilaapp_v3.viewmodel.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.dam.pi.vinilaapp_v3.ui.login.ui.GoogleAuthUiClient
import es.dam.pi.vinilaapp_v3.viewmodel.LoginViewModel
import es.dam.pi.vinilaapp_v3.viewmodel.UserViewModel

class LoginViewModelFactory(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userViewModel: UserViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(googleAuthUiClient, userViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
