package es.dam.pi.vinilaapp_v3.viewmodel.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import es.dam.pi.vinilaapp_v3.viewmodel.UserViewModel

class UserViewModelFactory(private val database: FirebaseDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}