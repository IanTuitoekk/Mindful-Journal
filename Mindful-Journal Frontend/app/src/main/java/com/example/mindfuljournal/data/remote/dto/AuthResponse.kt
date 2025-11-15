package com.example.mindfuljournal.data.remote.dto

data class AuthResponse(
    val message: String? = null,
    val error: String? = null,
    val user: User? = null
) {
    val success: Boolean
        get() = error == null && user != null
}

data class User(
    val id: Int,
    val username: String,
    val email: String
)