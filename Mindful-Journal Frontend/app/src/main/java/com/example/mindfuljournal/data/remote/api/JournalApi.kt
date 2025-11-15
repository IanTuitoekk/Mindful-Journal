package com.example.mindfuljournal.data.remote.api

import com.example.mindfuljournal.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface JournalApi {
    @POST("journals/create")
    suspend fun createJournal(@Body request: CreateJournalRequest): Response<JournalResponse>
    
    @GET("journals/read/{journal_id}")
    suspend fun getJournal(@Path("journal_id") journalId: Int): Response<JournalEntry>
    
    @PUT("journals/update/{journal_id}")
    suspend fun updateJournal(
        @Path("journal_id") journalId: Int,
        @Body request: UpdateJournalRequest
    ): Response<JournalResponse>
    
    @DELETE("journals/delete/{journal_id}")
    suspend fun deleteJournal(@Path("journal_id") journalId: Int): Response<JournalResponse>
    
     @GET("journals/user/{user_id}")
    suspend fun getUserJournals(@Path("user_id") userId: Int): Response<JournalListResponse>
}