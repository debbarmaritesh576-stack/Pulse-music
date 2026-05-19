package com.pulse.music.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ArtworkApi {
    @GET("api/v1/artwork")
    suspend fun getArtworkUrl(
        @Query("artist") artist: String,
        @Query("album") album: String
    ): ArtworkResponse
}

data class ArtworkResponse(
    val imageUrl: String?,
    val error: String?
)

class ArtworkService @Inject constructor(
    private val retrofitClient: RetrofitClient
) {
    private val api = retrofitClient.createService(
        ArtworkApi::class.java,
        "https://api.example.com/"
    )

    suspend fun fetchArtworkUrl(artist: String, album: String): Result<String> {
        return try {
            val response = api.getArtworkUrl(artist, album)
            if (response.imageUrl != null) {
                Result.success(response.imageUrl)
            } else {
                Result.failure(Exception(response.error ?: "No artwork found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}