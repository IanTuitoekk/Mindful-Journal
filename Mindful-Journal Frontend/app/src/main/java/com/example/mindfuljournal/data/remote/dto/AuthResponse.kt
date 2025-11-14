package com.example.mindfuljournal.data.remote.dto

data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null,
    val user: User? = null
)

data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val createdAt: String? = null
)