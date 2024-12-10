package com.example.notify_app.api

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object SpotifyAuthInstance {
    private const val BASE_URL = "https://accounts.spotify.com/"

    fun create(clientId: String, clientSecret: String): SpotifyAuthApi {
        val authInterceptor = Interceptor { chain ->
            val credentials = "$clientId:$clientSecret"
            val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", basicAuth)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()

            chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(SpotifyAuthApi::class.java)
    }
}