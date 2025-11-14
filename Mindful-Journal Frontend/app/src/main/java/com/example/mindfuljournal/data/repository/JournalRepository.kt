package com.example.mindfuljournal.data.repository

import com.example.mindfuljournal.data.dao.JournalDao
import com.example.mindfuljournal.data.database.entities.JournalEntry
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {

    val allEntries: Flow<List<JournalEntry>> = journalDao.getAllEntries()
    val favoriteEntries: Flow<List<JournalEntry>> = journalDao.getFavoriteEntries()

    suspend fun insert(entry: JournalEntry) {
        journalDao.insertEntry(entry)
    }

    suspend fun update(entry: JournalEntry) {
        journalDao.updateEntry(entry)
    }

    suspend fun delete(entry: JournalEntry) {
        journalDao.deleteEntry(entry)
    }

    suspend fun getEntryById(id: Int): JournalEntry? {
        return journalDao.getEntryById(id)
    }
}