package com.example.mindfuljournal.data.remote.dto

data class JournalResponse(
    val message: String? = null,
    val journal_id: Int? = null,
    val error: String? = null
)

data class JournalEntry(
    val id: Int,
    val user_id: Int,
    val title: String,
    val content: String,
    val created_at: String,
    val updated_at: String
)

data class JournalListResponse(
    val journals: List<JournalEntry>
)