package com.example.mindfuljournal.data.remote.api

import com.example.mindfuljournal.data.remote.dto.AuthResponse
import com.example.mindfuljournal.data.remote.dto.LoginRequest
import com.example.mindfuljournal.data.remote.dto.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users/register")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>
    
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}