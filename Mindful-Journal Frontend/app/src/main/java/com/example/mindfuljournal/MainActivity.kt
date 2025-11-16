package com.example.mindfuljournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mindfuljournal.data.local.preferences.TokenManager
import com.example.mindfuljournal.data.remote.api.RetrofitInstance
import com.example.mindfuljournal.data.repository.AuthRepository
import com.example.mindfuljournal.ui.navigation.AppNavigation
import com.example.mindfuljournal.ui.screens.auth.AuthViewModel
import com.example.mindfuljournal.ui.theme.MindfulJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val tokenManager = TokenManager(applicationContext)
        val authRepository = AuthRepository(RetrofitInstance.authApi, tokenManager)
        val authViewModel = AuthViewModel(authRepository)

        setContent {
            MindfulJournalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(authViewModel = authViewModel)  // ← Fixed: added parameter name
                }
            }
        }
    }
}