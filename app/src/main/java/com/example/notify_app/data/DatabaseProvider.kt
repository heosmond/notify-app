package com.example.notify_app.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var database: NotesDatabase? = null

    fun getDatabase(context: Context): NotesDatabase {
        return database ?: synchronized(this) { //thread safety
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "notes_database"
            ).build()
            database = instance
            instance
        }
    }
}