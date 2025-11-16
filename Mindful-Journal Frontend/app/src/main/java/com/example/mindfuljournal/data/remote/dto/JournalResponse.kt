package com.example.mindfuljournal.data.remote.dto

import com.google.gson.annotations.SerializedName

data class JournalResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("journal_id")
    val journal_id: Int? = null,

    @SerializedName("error")
    val error: String? = null
)

data class JournalEntry(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("mood")
    val mood: String? = null,  // Added mood field

    @SerializedName("created_at")
    val created_at: String,

    @SerializedName("updated_at")
    val updated_at: String
)

data class JournalListResponse(
    @SerializedName("journals")
    val journals: List<JournalEntry>
)