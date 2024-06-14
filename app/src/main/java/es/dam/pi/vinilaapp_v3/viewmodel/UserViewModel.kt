package es.dam.pi.vinilaapp_v3.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import es.dam.pi.vinilaapp_v3.data.api.ApiService
import es.dam.pi.vinilaapp_v3.data.network.NetworkModule
import es.dam.pi.vinilaapp_v3.ui.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
class UserViewModel(private val database: FirebaseDatabase) : ViewModel() {

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _imageUploadSuccess = MutableStateFlow<Boolean>(false)
    val imageUploadSuccess: StateFlow<Boolean> = _imageUploadSuccess

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val apiService: ApiService
    private val storageReference: StorageReference by lazy {
        FirebaseStorage.getInstance().reference.child("avatars")
    }

    init {
        val retrofit: Retrofit = NetworkModule.provideRetrofit()
        apiService = NetworkModule.provideApiService(retrofit)
    }

    private val _tempProfilePictureUrl = MutableStateFlow<String?>(null)
    val tempProfilePictureUrl: StateFlow<String?> = _tempProfilePictureUrl

    fun setTempProfilePictureUrl(url: String) {
        _tempProfilePictureUrl.value = url
    }

    fun clearTempProfilePictureUrl() {
        _tempProfilePictureUrl.value = null
    }

    fun saveProfilePicture() {
        val user = _user.value ?: return
        val tempUrl = _tempProfilePictureUrl.value ?: return
        updateUser(user.copy(profilePictureUrl = tempUrl))
        clearTempProfilePictureUrl()
    }

    fun cancelProfilePictureUpdate() {
        clearTempProfilePictureUrl()
    }

    fun uploadImageFromCamera(context: Context, bitmap: Bitmap) {
        viewModelScope.launch {
            val uri = bitmap.toUri(context)
            _selectedImageUri.value = uri
            _tempProfilePictureUrl.value = uri.toString()
        }
    }

    fun updateImageUrlFromGallery(uri: Uri) {
        viewModelScope.launch {
            _selectedImageUri.value = uri
            _tempProfilePictureUrl.value = uri.toString()
        }
    }

    private suspend fun uploadImage(uri: Uri) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val storageRef = storageReference.child("$userId/${uri.lastPathSegment}")
        try {
            storageRef.putFile(uri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            val currentUser = _user.value
            currentUser?.let { user ->
                val updatedUser = user.copy(profilePictureUrl = downloadUrl)
                updateUser(updatedUser)
                _imageUploadSuccess.emit(true)
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error uploading image to Firebase", e)
        }
    }

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            try {
                val user = database.getReference("users").child(userId).get().await()
                    .getValue(UserData::class.java)
                _user.value = user
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to fetch user", e)
            }
        }
    }

    fun loadUser(userId: String, user: FirebaseUser? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userSnapshot = database.getReference("users").child(userId).get().await()
                val userData = userSnapshot.getValue(UserData::class.java)
                if (userData != null) {
                    _user.value = userData
                } else if (user != null) {
                    val newUser = UserData(
                        userId = userId,
                        username = user.email?.substringBefore("@") ?: "Usuario",
                        profilePictureUrl = user.photoUrl?.toString()
                    )
                    database.getReference("users").child(userId).setValue(newUser).await()
                    _user.value = newUser
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to load user", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Rest of the functions...

    fun Bitmap.toUri(context: Context): Uri {
        val file = File(context.cacheDir, "temp_image_file.jpg")
        val bos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()
        FileOutputStream(file).use { fos ->
            fos.write(bitmapData)
            fos.flush()
        }
        return Uri.fromFile(file)
    }







    fun deleteUser(userId: String) {
        viewModelScope.launch {
            try {
                database.getReference("users").child(userId).removeValue().await()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to delete user", e)
            }
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _user.value = null
    }

    fun updateUser(user: UserData) {
        viewModelScope.launch {
            try {
                database.getReference("users").child(user.userId).setValue(user).await()
                fetchUser(user.userId)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to update user", e)
            }
        }
    }

    fun deleteAvatar() {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                val user = _user.value ?: return@launch

                val storageRef = storageReference.child(userId)
                storageRef.delete().await()

                val updatedUser = user.copy(profilePictureUrl = null)
                updateUser(updatedUser)

            } catch (e: Exception) {
                Log.e("UserViewModel", "Error deleting avatar", e)
            }
        }
    }

    fun createUser(user: UserData) {
        viewModelScope.launch {
            try {
                val existingUser = database.getReference("users").child(user.userId).get().await()
                    .getValue(UserData::class.java)
                if (existingUser == null) {
                    database.getReference("users").child(user.userId).setValue(user).await()
                    fetchUser(user.userId)
                } else {
                    Log.d("UserViewModel", "El usuario ${user.userId} ya existe.")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to create user", e)
            }
        }
    }

    fun resetImageUploadSuccess() {
        _imageUploadSuccess.value = false
    }

    fun clearUser() {
        viewModelScope.launch {
            _user.value = null
        }
    }

    // Añadir el método que falta: updateSelectedImageUri
    fun updateSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    // Añadir la función de extensión para Bitmap

}
