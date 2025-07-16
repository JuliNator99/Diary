package de.julianschwers.diary.data.database

import kotlinx.coroutines.flow.Flow

interface JournalEntryDao {
    fun getJournal(uid: String): JournalEntryData
    fun queryJournals(): Flow<List<JournalEntryData>>
    
    fun upsert(journal: JournalEntryData)
    fun delete(journal: JournalEntryData)
}