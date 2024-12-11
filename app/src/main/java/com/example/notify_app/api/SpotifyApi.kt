package com.example.notify_app.api

import com.example.notify_app.api.data.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApi {
    @GET("v1/search")
    suspend fun getTrackResults(
        //query parameters for search
        @Header("Authorization") authToken: String,
        @Query("q") query: String, //need to format query with
        @Query("type") type: String = "track"
    ) : Response<SearchResponse>
}