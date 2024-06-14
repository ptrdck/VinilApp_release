package es.dam.pi.vinilaapp_v3.data.network

import com.google.firebase.storage.FirebaseStorage
import es.dam.pi.vinilaapp_v3.data.api.ApiService
import es.dam.pi.vinilaapp_v3.data.api.ApiServiceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://vinilapp-c328e-default-rtdb.europe-west1.firebasedatabase.app/") // URL de tu Realtime Database
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideApiService(retrofit: Retrofit): ApiService {
        return ApiServiceImpl(retrofit)
    }

    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}
