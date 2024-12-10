package com.example.notify_app.api

import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyApi {
    @GET("v1/search/")
    suspend fun getTrackResults(
        //query parameters for search
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int //limit of items to return
    ) : Tracks //TODO: build data classes
}