package com.example.mindfuljournal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindfuljournal.data.repository.JournalRepository
import com.example.mindfuljournal.data.remote.dto.JournalEntry
import com.example.mindfuljournal.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JournalState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class JournalViewModel(
    private val journalRepository: JournalRepository
) : ViewModel() {
    
    private val _journalState = MutableStateFlow(JournalState())
    val journalState: StateFlow<JournalState> = _journalState.asStateFlow()
    
    private val _journals = MutableStateFlow<List<JournalEntry>>(emptyList())
    val journals: StateFlow<List<JournalEntry>> = _journals.asStateFlow()
    
    fun createJournal(title: String, content: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            journalRepository.createJournal(title, content).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _journalState.value = JournalState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _journalState.value = JournalState(successMessage = result.data)
                        loadUserJournals() // Reload journals after creating
                        onSuccess()
                    }
                    is Resource.Error -> {
                        _journalState.value = JournalState(error = result.message)
                    }
                }
            }
        }
    }
    
    fun updateJournal(journalId: Int, title: String?, content: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            journalRepository.updateJournal(journalId, title, content).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _journalState.value = JournalState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _journalState.value = JournalState(successMessage = result.data)
                        loadUserJournals() // Reload journals after updating
                        onSuccess()
                    }
                    is Resource.Error -> {
                        _journalState.value = JournalState(error = result.message)
                    }
                }
            }
        }
    }
    
    fun deleteJournal(journalId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            journalRepository.deleteJournal(journalId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _journalState.value = JournalState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _journalState.value = JournalState(successMessage = result.data)
                        loadUserJournals() // Reload journals after deleting
                        onSuccess()
                    }
                    is Resource.Error -> {
                        _journalState.value = JournalState(error = result.message)
                    }
                }
            }
        }
    }
    
    fun loadUserJournals() {
        viewModelScope.launch {
            journalRepository.getUserJournals().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _journalState.value = JournalState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _journals.value = result.data ?: emptyList()
                        _journalState.value = JournalState()
                    }
                    is Resource.Error -> {
                        _journalState.value = JournalState(error = result.message)
                    }
                }
            }
        }
    }
    
    fun resetState() {
        _journalState.value = JournalState()
    }
}