package com.example.notify_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val song: String,
    val artist: String,
    val year: String,
    val genre: String,
    val content: String,
    val imagePath: String?,
    val lastModified: Long
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf<String>(
            "$title$content",
            "$title $content",
            "${title.first()} ${content.first()}"
        )
        return matchingCombinations.any {
            it.contains(query, true)
        }
    }
}