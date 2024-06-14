package es.dam.pi.vinilaapp_v3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NewsViewModel : ViewModel() {
    private val _newsImages = MutableStateFlow<List<String>>(emptyList())
    val newsImages: StateFlow<List<String>> = _newsImages

    init {
        loadNewsImages()
    }

    private fun loadNewsImages() {
        viewModelScope.launch {
            try {
                val storage = FirebaseStorage.getInstance().reference.child("novedades")
                val result = storage.listAll().await()
                val imageUrls = result.items.map { it.downloadUrl.await().toString() }
                _newsImages.value = imageUrls
            } catch (e: Exception) {
                // Manejo de errores
                e.printStackTrace()
            }
        }
    }
}
