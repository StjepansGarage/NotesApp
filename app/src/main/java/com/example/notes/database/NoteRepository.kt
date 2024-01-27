package com.example.notes.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun insert(note: NoteEntity) {
        withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    fun searchNotesByTitle(title: String): LiveData<List<NoteEntity>> {
        // Izravno vratite LiveData, ne trebate koristiti withContext jer query nije suspend funkcija
        return noteDao.searchNotesByTitle(title)
    }

    suspend fun delete(note: NoteEntity) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}
