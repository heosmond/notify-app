package com.example.notify_app.api

import com.example.notify_app.BuildConfig

//HOOKS INTO VIEW MODEL
class SpotifyRepository {
    private val spotifyAuthApi: SpotifyAuthApi = SpotifyAuthInstance.create(
        BuildConfig.SPOTIFY_CLIENT_ID, //NOTE: Stored in local.properties (you need to manually add these to your gradle configs) - can't expose in public repo
        BuildConfig.SPOTIFY_CLIENT_SECRET
    )

    suspend fun fetchAccessToken(): String? {
        return try {
            val response = spotifyAuthApi.getAccessToken()
            if (response.isSuccessful) {
                response.body()?.accessToken
            } else {
                null //TODO handle error
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}