package com.pulse.music.network

import retrofit2.http.GET
import retrofit2.http.Query

interface LyricsApi {
    @GET("api/v1/lyrics")
    suspend fun getLyrics(
        @Query("artist") artist: String,
        @Query("title") title: String
    ): LyricsResponse
}

data class LyricsResponse(
    val lyrics: String?,
    val error: String?
)

class LyricsService @Inject constructor(
    private val retrofitClient: RetrofitClient
) {
    private val api = retrofitClient.createService(
        LyricsApi::class.java,
        "https://api.example.com/"
    )

    suspend fun fetchLyrics(artist: String, title: String): Result<String> {
        return try {
            val response = api.getLyrics(artist, title)
            if (response.lyrics != null) {
                Result.success(response.lyrics)
            } else {
                Result.failure(Exception(response.error ?: "No lyrics found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}