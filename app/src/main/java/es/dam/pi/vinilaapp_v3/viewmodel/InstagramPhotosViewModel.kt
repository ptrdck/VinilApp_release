package es.dam.pi.vinilaapp_v3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.pi.vinilaapp_v3.data.api.InstagramApiService
import es.dam.pi.vinilaapp_v3.ui.model.InstagramPhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InstagramPhotosViewModel(
    private val instagramApiService: InstagramApiService,
    private val instagramAccessToken: String
) : ViewModel() {

    private val _instagramPhotos = MutableStateFlow<List<InstagramPhoto>>(emptyList())
    val instagramPhotos: StateFlow<List<InstagramPhoto>> = _instagramPhotos

    init {
        fetchInstagramPhotos()
    }

    private fun fetchInstagramPhotos() {
        viewModelScope.launch {
            try {
                val response = instagramApiService.getInstagramPhotos(accessToken = instagramAccessToken)
                _instagramPhotos.value = response.data
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
