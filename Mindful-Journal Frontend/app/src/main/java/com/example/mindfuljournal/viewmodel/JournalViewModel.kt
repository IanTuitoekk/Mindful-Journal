package com.example.mindfuljournal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindfuljournal.data.database.JournalDatabase
import com.example.mindfuljournal.data.database.entities.JournalEntry
import com.example.mindfuljournal.data.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JournalRepository
    val allEntries: Flow<List<JournalEntry>>

    init {
        val dao = JournalDatabase.getDatabase(application).journalDao()
        repository = JournalRepository(dao)
        allEntries = repository.allEntries
    }

    fun insertEntry(entry: JournalEntry) = viewModelScope.launch {
        repository.insert(entry)
    }

    fun updateEntry(entry: JournalEntry) = viewModelScope.launch {
        repository.update(entry)
    }

    fun deleteEntry(entry: JournalEntry) = viewModelScope.launch {
        repository.delete(entry)
    }
}