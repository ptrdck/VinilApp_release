package es.dam.pi.vinilaapp_v3.data.api

import es.dam.pi.vinilaapp_v3.data.network.ImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("user/avatar")
    fun getAvatar(): Call<List<ImageResponse>>

    @Multipart
    @POST("user/avatar")
    fun uploadAvatar(@Part image: MultipartBody.Part): Call<ImageResponse>

    @DELETE("user/avatar")
    fun deleteAvatar(): Call<Void>

    @GET("photos")
    suspend fun getPhotos(): List<String>

    @GET("aboutus")
    suspend fun getAboutUs(): List<ImageResponse>
}
