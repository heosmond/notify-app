package com.example.notify_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    //attempts insert, if conflict it will update
    @Upsert
    suspend fun upsertNote(note: Note) : Long

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY lastModified DESC")
    fun getNotesOrderedByLastModified(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY title DESC")
    fun getNotesOrderedByTitle(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note?
}