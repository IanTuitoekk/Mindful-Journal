package com.example.mindfuljournal.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateJournalRequest(
    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("mood")
    val mood: String? = null  // Add mood field
)

data class UpdateJournalRequest(
    @SerializedName("title")
    val title: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("mood")
    val mood: String? = null  // Add mood field
)