package com.example.notify_app.api.data

data class Track(
    val album: Album,
    val artists: List<Artist>,
    val duration_ms: Int,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)