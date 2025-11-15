package com.example.mindfuljournal.data.remote.dto

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)