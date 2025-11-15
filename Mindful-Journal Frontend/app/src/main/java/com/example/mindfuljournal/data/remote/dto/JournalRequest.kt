package com.example.mindfuljournal.data.remote.dto

data class CreateJournalRequest(
    val user_id: Int,
    val title: String,
    val content: String
)

data class UpdateJournalRequest(
    val title: String?,
    val content: String?
)