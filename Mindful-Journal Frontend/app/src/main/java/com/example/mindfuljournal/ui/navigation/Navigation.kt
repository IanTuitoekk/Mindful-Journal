package com.example.mindfuljournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindfuljournal.data.local.preferences.TokenManager
import com.example.mindfuljournal.data.remote.api.RetrofitInstance
import com.example.mindfuljournal.data.repository.JournalRepository
import com.example.mindfuljournal.ui.screens.auth.AuthViewModel
import com.example.mindfuljournal.ui.screens.auth.LoginScreen
import com.example.mindfuljournal.ui.screens.auth.SignUpScreen
import com.example.mindfuljournal.ui.screens.home.HomeScreen
import com.example.mindfuljournal.ui.screens.home.JournalEntry as HomeJournalEntry
import com.example.mindfuljournal.ui.screens.entry.NewEntryScreen
import com.example.mindfuljournal.viewmodel.JournalViewModel
import com.example.mindfuljournal.viewmodel.JournalViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object NewEntry : Screen("new_entry")
    object MoodLog : Screen("mood_log")
}

@Composable
fun AppNavigation(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    
    // Get username from TokenManager
    val username by tokenManager.getUsername().collectAsState(initial = "User")
    
    // Create JournalViewModel
    val journalRepository = JournalRepository(
        RetrofitInstance.journalApi,
        tokenManager
    )
    val journalViewModel: JournalViewModel = viewModel(
        factory = JournalViewModelFactory(journalRepository)
    )
    
    // Collect journals from ViewModel
    val apiJournals by journalViewModel.journals.collectAsState()
    
    // Convert API journals to Home screen format
    val homeJournals = apiJournals.map { apiJournal ->
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        
        val formattedDate = try {
            val date = inputFormat.parse(apiJournal.created_at)
            dateFormat.format(date ?: java.util.Date())
        } catch (e: Exception) {
            "Unknown"
        }
        
        HomeJournalEntry(
            id = apiJournal.id,
            title = apiJournal.title,
            mood = "😊 Happy", // Default mood since backend doesn't have it yet
            date = formattedDate
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    journalViewModel.loadUserJournals()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                viewModel = viewModel,
                onSignUpSuccess = {
                    journalViewModel.loadUserJournals()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            // Load journals when entering home screen
            LaunchedEffect(Unit) {
                journalViewModel.loadUserJournals()
            }
            
            HomeScreen(
                navController = navController,
                username = username ?: "User",
                journalEntries = homeJournals
            )
        }

        composable(Screen.NewEntry.route) {
            NewEntryScreen(
                onNavigateBack = { navController.popBackStack() },
                onSave = { title, mood, content ->
                    journalViewModel.createJournal(title, content) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}