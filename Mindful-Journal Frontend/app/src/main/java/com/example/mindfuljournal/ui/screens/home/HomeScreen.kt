package com.example.mindfuljournal.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mindfuljournal.data.remote.dto.JournalEntry
import com.example.mindfuljournal.viewmodel.JournalViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    username: String,
    viewModel: JournalViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadUserJournals()
    }

    val journals by viewModel.journals.collectAsState()

    val prompts = listOf(
        "What's something you're grateful for today?",
        "What emotion stood out most to you this week?",
        "How did you take care of yourself today?",
        "What's something you'd like to let go of?",
        "What small victory did you achieve today?"
    )
    val randomPrompt = remember { prompts.random() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("new_entry") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Entry")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Hi, $username 👋",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Welcome back to your mindful space",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MoodLogSection()
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                PromptCard(randomPrompt)
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                JournalStats(journals)
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                ReminderCard()
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = "Recent Journal Entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(journals) { entry ->
                JournalCard(
                    entry = entry,
                    onClick = {
                        navController.navigate("edit_entry/${entry.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun MoodLogSection() {
    val moods = listOf("😄", "🙂", "😐", "😔", "😢")
    var selectedMood by remember { mutableStateOf<String?>(null) }

    Column {
        Text(
            text = "Log Your Mood",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            moods.forEach { mood ->
                TextButton(onClick = { selectedMood = mood }) {
                    Text(text = mood, style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
        selectedMood?.let {
            Text(text = "Mood selected: $it", color = Color.Gray)
        }
    }
}

@Composable
fun PromptCard(prompt: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Today's Prompt",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(prompt, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun JournalStats(entries: List<JournalEntry>) {
    val total = entries.size
    val randomCount = Random.nextInt(1, 5)
    val lastDate = entries.firstOrNull()?.created_at?.let { formatDate(it) } ?: "N/A"

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Journal Summary", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Entries this week: $randomCount")
            Text("Total entries: $total")
            Text("Last entry: $lastDate")
        }
    }
}

@Composable
fun ReminderCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Next Reminder",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("You have a journaling reminder at 8:00 PM tonight 🕗")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Edit Reminder")
            }
        }
    }
}

@Composable
fun JournalCard(entry: JournalEntry, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Mood: ${entry.mood ?: "Not specified"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = formatDate(entry.created_at),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        // Flask returns: "2025-11-16 10:35:40"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: java.util.Date())
    } catch (e: Exception) {
        dateString
    }
}