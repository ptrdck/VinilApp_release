package es.dam.pi.vinilaapp_v3.data.api

import com.google.firebase.storage.FirebaseStorage
import es.dam.pi.vinilaapp_v3.data.network.ImageResponse
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit

class ApiServiceImpl(retrofit: Retrofit) : ApiService {
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference = firebaseStorage.reference.child("aboutus")

    private val retrofitService = retrofit.create(ApiService::class.java)

    override fun getAvatar(): Call<List<ImageResponse>> {
        return retrofitService.getAvatar()
    }

    override fun uploadAvatar(image: MultipartBody.Part): Call<ImageResponse> {
        return retrofitService.uploadAvatar(image)
    }

    override fun deleteAvatar(): Call<Void> {
        return retrofitService.deleteAvatar()
    }

    override suspend fun getPhotos(): List<String> {
        return retrofitService.getPhotos()
    }

    override suspend fun getAboutUs(): List<ImageResponse> {
        val images = mutableListOf<ImageResponse>()
        val result = storageReference.listAll().await()
        for (fileRef in result.items) {
            val url = fileRef.downloadUrl.await().toString()
            images.add(ImageResponse(imageUrl = url))
        }
        return images
    }
}
