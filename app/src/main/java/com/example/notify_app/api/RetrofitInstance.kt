package com.example.notify_app.api

import com.example.notify_app.BuildConfig
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.spotify.com/"
    val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET

    fun create(authToken: String): SpotifyApi {
        //create interceptor to add auth token to each request
        val authInterceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
            chain.proceed(request)
        }

        //build okHttpClient with interceptor
        val okHttpClient = okhttp3.OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        //build retrofit with okHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(SpotifyApi::class.java)
    }
}