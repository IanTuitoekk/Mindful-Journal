package com.example.mindfuljournal.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,
    val mood: String,
    val date: Long = System.currentTimeMillis(),
    val tags: String? = null,
    val isFavorite: Boolean = false
)