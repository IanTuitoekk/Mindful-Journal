package com.example.mindfuljournal.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.random.Random

data class JournalEntry(
    val id: Int,
    val title: String,
    val mood: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    username: String,
    journalEntries: List<JournalEntry> = sampleEntries()
) {
    val prompts = listOf(
        "What’s something you’re grateful for today?",
        "What emotion stood out most to you this week?",
        "How did you take care of yourself today?",
        "What’s something you’d like to let go of?",
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

            // Greeting
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

            // Quick Mood Log
            item {
                MoodLogSection()
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Guided Prompt
            item {
                PromptCard(randomPrompt)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Journal Stats
            item {
                JournalStats(journalEntries)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Reminder Section
            item {
                ReminderCard()
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Recent Entries
            item {
                Text(
                    text = "Recent Journal Entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(journalEntries) { entry ->
                JournalCard(entry)
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
                    Text(
                        text = mood,
                        style = MaterialTheme.typography.headlineMedium
                    )
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
    val lastDate = entries.firstOrNull()?.date ?: "N/A"

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
                onClick = { /* TODO: Set new reminder */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Edit Reminder")
            }
        }
    }
}

@Composable
fun JournalCard(entry: JournalEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { /* TODO: Navigate to entry details */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Mood: ${entry.mood}", style = MaterialTheme.typography.bodyMedium)
            Text(text = entry.date, style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun sampleEntries(): List<JournalEntry> = listOf(
    JournalEntry(1, "Grateful Morning", "😊 Happy", "Nov 6, 2025"),
    JournalEntry(2, "Tired but Hopeful", "😴 Tired", "Nov 5, 2025"),
    JournalEntry(3, "Peaceful Walk", "🙂 Calm", "Nov 4, 2025")
)
