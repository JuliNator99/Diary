package de.julianschwers.diary.data.database

import kotlinx.coroutines.flow.Flow

interface JournalEntryDao {
    suspend fun getJournal(uid: String): JournalEntryData?
    fun queryJournals(): Flow<List<JournalEntryData>>
    
    suspend fun upsert(journal: JournalEntryData)
    suspend fun delete(journal: JournalEntryData)
}