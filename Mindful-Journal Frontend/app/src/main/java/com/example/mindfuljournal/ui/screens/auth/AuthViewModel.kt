package com.example.mindfuljournal.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindfuljournal.data.repository.AuthRepository
import com.example.mindfuljournal.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.signUp(username, email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _authState.value = AuthState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _authState.value = AuthState(
                            isSuccess = true,
                            successMessage = result.data
                        )
                    }
                    is Resource.Error -> {
                        _authState.value = AuthState(error = result.message)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _authState.value = AuthState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _authState.value = AuthState(
                            isSuccess = true,
                            successMessage = result.data
                        )
                    }
                    is Resource.Error -> {
                        _authState.value = AuthState(error = result.message)
                    }
                }
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState()
    }
}