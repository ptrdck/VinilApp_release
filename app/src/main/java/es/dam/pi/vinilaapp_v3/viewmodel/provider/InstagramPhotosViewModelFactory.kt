package es.dam.pi.vinilaapp_v3.viewmodel.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.dam.pi.vinilaapp_v3.data.api.InstagramApiService
import es.dam.pi.vinilaapp_v3.viewmodel.InstagramPhotosViewModel

class InstagramPhotosViewModelFactory(
    private val instagramApiService: InstagramApiService,
    private val instagramAccessToken: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InstagramPhotosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InstagramPhotosViewModel(instagramApiService, instagramAccessToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
