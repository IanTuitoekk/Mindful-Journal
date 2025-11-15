package com.example.mindfuljournal.data.repository

import com.example.mindfuljournal.data.local.preferences.TokenManager
import com.example.mindfuljournal.data.remote.api.JournalApi
import com.example.mindfuljournal.data.remote.dto.CreateJournalRequest
import com.example.mindfuljournal.data.remote.dto.UpdateJournalRequest
import com.example.mindfuljournal.data.remote.dto.JournalEntry
import com.example.mindfuljournal.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first

class JournalRepository(
    private val journalApi: JournalApi,
    private val tokenManager: TokenManager
) {
    fun createJournal(title: String, content: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            
            // Get user ID from token manager
            val userId = tokenManager.getUserId().first()?.toIntOrNull()
            
            if (userId == null) {
                emit(Resource.Error("User not logged in"))
                return@flow
            }
            
            val request = CreateJournalRequest(
                user_id = userId,
                title = title,
                content = content
            )
            
            val response = journalApi.createJournal(request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.error == null) {
                    emit(Resource.Success(body.message ?: "Journal created successfully"))
                } else {
                    emit(Resource.Error(body.error))
                }
            } else {
                emit(Resource.Error("Failed to create journal"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
    
    fun updateJournal(journalId: Int, title: String?, content: String?): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            
            val request = UpdateJournalRequest(title, content)
            val response = journalApi.updateJournal(journalId, request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.error == null) {
                    emit(Resource.Success(body.message ?: "Journal updated successfully"))
                } else {
                    emit(Resource.Error(body.error))
                }
            } else {
                emit(Resource.Error("Failed to update journal"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
    
    fun deleteJournal(journalId: Int): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            
            val response = journalApi.deleteJournal(journalId)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.error == null) {
                    emit(Resource.Success(body.message ?: "Journal deleted successfully"))
                } else {
                    emit(Resource.Error(body.error))
                }
            } else {
                emit(Resource.Error("Failed to delete journal"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
    
    fun getUserJournals(): Flow<Resource<List<JournalEntry>>> = flow {
        try {
            emit(Resource.Loading())
            
            val userId = tokenManager.getUserId().first()?.toIntOrNull()
            
            if (userId == null) {
                emit(Resource.Error("User not logged in"))
                return@flow
            }
            
            val response = journalApi.getUserJournals(userId)
            
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.journals))
            } else {
                emit(Resource.Error("Failed to fetch journals"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
}