package es.dam.pi.vinilaapp_v3.data.api

import es.dam.pi.vinilaapp_v3.ui.model.InstagramResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface InstagramApiService {

    @GET("me/media")
    suspend fun getInstagramPhotos(
        @Query("fields") fields: String = "id,media_type,media_url",
        @Query("access_token") accessToken: String
    ): InstagramResponse
}
