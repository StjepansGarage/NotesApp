package com.example.notes.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository = NoteRepository(AppDatabase.getDatabase(application).noteDao())

    val allNotes: LiveData<List<NoteEntity>> = repository.allNotes
    private val _searchResults = MutableLiveData<List<NoteEntity>>()
    val searchResults: LiveData<List<NoteEntity>> get() = _searchResults

    fun insert(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun delete(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun searchByTitle(title: String) = viewModelScope.launch {
        val searchResults = repository.searchNotesByTitle(title)
        _searchResults.postValue(searchResults.value)
    }
}
