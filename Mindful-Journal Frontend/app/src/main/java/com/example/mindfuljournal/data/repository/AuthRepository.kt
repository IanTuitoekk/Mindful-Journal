package com.example.mindfuljournal.data.repository

import com.example.mindfuljournal.data.local.preferences.TokenManager
import com.example.mindfuljournal.data.remote.api.AuthApi
import com.example.mindfuljournal.data.remote.dto.LoginRequest
import com.example.mindfuljournal.data.remote.dto.SignUpRequest
import com.example.mindfuljournal.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    fun signUp(email: String, password: String, name: String? = null): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.signUp(SignUpRequest(email, password, name))

            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()!!
                authResponse.token?.let { tokenManager.saveAuthToken(it) }
                authResponse.user?.let {
                    tokenManager.saveUserInfo(it.email, it.id)
                }
                emit(Resource.Success(authResponse.message ?: "Sign up successful"))
            } else {
                emit(Resource.Error(response.body()?.message ?: "Sign up failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    fun login(email: String, password: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()!!
                authResponse.token?.let { tokenManager.saveAuthToken(it) }
                authResponse.user?.let {
                    tokenManager.saveUserInfo(it.email, it.id)
                }
                emit(Resource.Success(authResponse.message ?: "Login successful"))
            } else {
                emit(Resource.Error(response.body()?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    suspend fun logout() {
        tokenManager.clearTokens()
    }

    fun isLoggedIn(): Flow<Boolean> = flow {
        tokenManager.getAuthToken().collect { token ->
            emit(!token.isNullOrEmpty())
        }
    }
}