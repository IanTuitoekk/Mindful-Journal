package com.example.mindfuljournal.ui.screens.entry

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(
    entryId: Int? = null,
    initialTitle: String = "",
    initialMood: String = "",
    initialContent: String = "",
    onNavigateBack: () -> Unit,
    onSave: (String, String, String) -> Unit,
    onDelete: ((Int) -> Unit)? = null
) {
    var title by remember { mutableStateOf(initialTitle) }
    var mood by remember { mutableStateOf(initialMood) }
    var content by remember { mutableStateOf(initialContent) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val isEditing = entryId != null
    val moods = listOf("😄 Happy", "🙂 Calm", "😐 Neutral", "😔 Sad", "😢 Upset")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Entry" else "New Journal Entry") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditing && onDelete != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSave(title, mood.ifBlank { "😐 Neutral" }, content)
                    }
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                placeholder = { Text("Give your entry a title...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mood Selector
            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                moods.forEach { moodOption ->
                    FilterChip(
                        selected = mood == moodOption,
                        onClick = { mood = moodOption },
                        label = { Text(moodOption.split(" ")[0]) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content Field
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Your thoughts...") },
                placeholder = { Text("Write about your day, feelings, or anything on your mind...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 10
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSave(title, mood.ifBlank { "😐 Neutral" }, content)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text(if (isEditing) "Update Entry" else "Save Entry")
            }
        }
    }

    // Delete Confirmation Dialog

    if (showDeleteDialog && entryId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Entry?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete?.invoke(entryId)  // This calls the delete and navigates back
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}