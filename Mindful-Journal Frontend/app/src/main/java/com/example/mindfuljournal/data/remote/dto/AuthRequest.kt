package com.example.mindfuljournal.data.remote.dto

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)