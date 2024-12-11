package com.example.notify_app.api.data

import com.google.gson.annotations.SerializedName

data class Tracks(
    val href: String = "",
    @SerializedName("items")
    val tracks: List<Track>,
//    val limit: Int,
//    val next: String,
//    val offset: Int,
//    val previous: Any,
//    val total: Int
)