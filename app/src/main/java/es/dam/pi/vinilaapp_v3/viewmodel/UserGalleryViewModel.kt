package es.dam.pi.vinilaapp_v3.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import es.dam.pi.vinilaapp_v3.ui.model.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserGalleryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val _photos = MutableStateFlow<List<Image>>(emptyList())
    val photos: StateFlow<List<Image>> = _photos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        fetchUserPhotos()
    }

    fun fetchUserPhotos() { // Cambiado a público
        userId?.let {
            firestore.collection("users").document(it).collection("photos")
                .addSnapshotListener { result, error ->
                    if (error != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    val photoList = result?.documents?.map { document -> document.toObject(Image::class.java)!! }
                    _photos.value = photoList ?: emptyList()
                }
        }
    }

    fun uploadPhoto(uri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid
            val storageRef = FirebaseStorage.getInstance().reference.child("gallery/$userId/${uri.lastPathSegment}")
            _loading.value = true
            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { url ->
                    val photo = Image(imageId = firestore.collection("images").document().id, galleryId = "galleryId1", imageUrl = url.toString())
                    savePhotoToFirestore(photo)
                }.addOnCompleteListener {
                    _loading.value = false
                }
            }.addOnFailureListener {
                _loading.value = false
                // Handle error
            }
        } else {
            // Handle unauthenticated user
        }
    }

    private fun savePhotoToFirestore(photo: Image) {
        userId?.let {
            val docRef = firestore.collection("users").document(it).collection("photos").document(photo.imageId)
            docRef.set(photo)
        }
    }

    fun deleteSelectedPhotos(photoIds: List<String>) {
        userId?.let { userId ->
            photoIds.forEach { photoId ->
                val docRef = firestore.collection("users").document(userId).collection("photos").document(photoId)
                docRef.delete().addOnSuccessListener {
                    val storageRef = storage.reference.child("gallery/$userId/$photoId")
                    storageRef.delete()
                }
            }
        }
    }

    fun onPhotoTaken(uri: Uri) {
        uploadPhoto(uri)
    }

    fun onPhotoTakingFailed() {
        // Handle photo taking failure
    }
}
