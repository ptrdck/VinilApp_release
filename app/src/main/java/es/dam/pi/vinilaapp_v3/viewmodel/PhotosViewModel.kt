package es.dam.pi.vinilaapp_v3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.dam.pi.vinilaapp_v3.R
import es.dam.pi.vinilaapp_v3.data.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PhotosViewModel(private val apiService: ApiService) : ViewModel() {

    private val _photos = MutableStateFlow<List<Any>>(emptyList())
    val photos: StateFlow<List<Any>> = _photos

    init {
        fetchPhotos()
    }

    private fun fetchPhotos() {
        viewModelScope.launch {
            val storageReference = Firebase.storage.reference.child("aboutus")
            val localPhotos = listOf(
                R.drawable.vinilos1,
                R.drawable.vinilos2,
                R.drawable.vinilos3
            )

            try {
                val remotePhotos = mutableListOf<String>()
                val result = storageReference.listAll().await()
                for (fileRef in result.items) {
                    val url = fileRef.downloadUrl.await().toString()
                    remotePhotos.add(url)
                }
                _photos.value = localPhotos + remotePhotos
            } catch (e: Exception) {
                _photos.value = localPhotos // En caso de error, solo se cargan las fotos locales
            }
        }
    }
}
