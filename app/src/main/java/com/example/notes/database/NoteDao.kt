package com.example.notes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes.database.NoteEntity

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Update
    suspend fun update(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): NoteEntity?

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE naslov LIKE '%' || :title || '%'")
    fun searchNotesByTitle(title: String): LiveData<List<NoteEntity>>
}
