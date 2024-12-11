package com.example.notify_app.api

import com.example.notify_app.BuildConfig
import com.example.notify_app.api.data.SearchResponse

//HOOKS INTO VIEW MODEL
class SpotifyRepository {
    private val spotifyAuthApi: SpotifyAuthApi = SpotifyAuthInstance.create(
        BuildConfig.SPOTIFY_CLIENT_ID, //NOTE: Stored in local.properties (you need to manually add these to your gradle configs) - can't expose in public repo
        BuildConfig.SPOTIFY_CLIENT_SECRET
    )
    private var accessToken: String? = null
    private var spotifyApi: SpotifyApi? = null

    suspend fun fetchAccessToken(): String? {
        return try {
            val response = spotifyAuthApi.getAccessToken()
            if (response.isSuccessful) {
                val token = response.body()?.accessToken

                if (token != null) {
                    // Save the access token and create a new SpotifyApi instance with the token
                    accessToken = token
                    spotifyApi = SpotifyInstance.create(token)  //CREATES NEW API WITH NEW TOKEN
                    return token
                }

                null //TODO handle null token

            } else {
                null //TODO handle network error
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getSearchResults(query: String): SearchResponse? {
        return accessToken?.let {
            val response = spotifyApi?.getTrackResults(it, query = query)
            if (response?.isSuccessful == true) {
                response.body()
            } else {
                throw Exception("API Error: ${response?.errorBody()?.string()}")
            }
        }
    }

}