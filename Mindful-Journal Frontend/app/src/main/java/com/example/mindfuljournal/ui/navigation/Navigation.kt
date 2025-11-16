package com.example.mindfuljournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mindfuljournal.data.local.preferences.TokenManager
import com.example.mindfuljournal.data.remote.api.RetrofitInstance
import com.example.mindfuljournal.data.repository.JournalRepository
import com.example.mindfuljournal.ui.screens.auth.AuthViewModel
import com.example.mindfuljournal.ui.screens.auth.LoginScreen
import com.example.mindfuljournal.ui.screens.auth.SignUpScreen
import com.example.mindfuljournal.ui.screens.home.HomeScreen
import com.example.mindfuljournal.ui.screens.entry.NewEntryScreen
import com.example.mindfuljournal.viewmodel.JournalViewModel
import com.example.mindfuljournal.viewmodel.JournalViewModelFactory

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = TokenManager(context)

    val username by tokenManager.getUsername().collectAsState(initial = "User")

    val journalRepository = JournalRepository(
        RetrofitInstance.journalApi,
        tokenManager
    )
    val journalViewModel: JournalViewModel = viewModel(
        factory = JournalViewModelFactory(journalRepository)
    )

    val apiJournals by journalViewModel.journals.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    journalViewModel.loadUserJournals()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = {
                    journalViewModel.loadUserJournals()
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            LaunchedEffect(Unit) {
                journalViewModel.loadUserJournals()
            }

            HomeScreen(
                navController = navController,
                username = username ?: "User",
                viewModel = journalViewModel
            )
        }

        composable("new_entry") {
            NewEntryScreen(
                onNavigateBack = { navController.popBackStack() },
                onSave = { title, mood, content ->
                    journalViewModel.createJournal(title, content, mood) {  // Pass mood
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(
            route = "edit_entry/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getInt("entryId")
            val entry = apiJournals.find { it.id == entryId }

            if (entry != null && entryId != null) {
                NewEntryScreen(
                    entryId = entryId,
                    initialTitle = entry.title,
                    initialMood = entry.mood ?: "",
                    initialContent = entry.content,
                    onNavigateBack = { navController.popBackStack() },
                    onSave = { title, mood, content ->
                        journalViewModel.updateJournal(entryId, title, content, mood) {  // Pass mood
                            navController.popBackStack()
                        }
                    },
                    onDelete = { id ->
                        journalViewModel.deleteJournal(id) {
                            navController.popBackStack()
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}