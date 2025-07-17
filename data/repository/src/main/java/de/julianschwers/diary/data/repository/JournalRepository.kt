package de.julianschwers.diary.data.repository

import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.model.Mood
import de.julianschwers.diary.data.database.Database
import de.julianschwers.diary.data.database.JournalEntryDataImpl
import de.julianschwers.diary.data.database.copyImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class JournalRepository(private val database: Database) {
    suspend fun getJournal(uid: String): JournalEntry? = database.journalEntryDao.getJournal(uid = uid)?.asModel()
    fun queryJournals(): Flow<List<JournalEntry>> = database.journalEntryDao.queryJournals().map { flow -> flow.map { it.asModel() } }
    
    suspend fun upsert(journal: JournalEntry) = database.journalEntryDao.upsert(journal = journal.asData(database.journalEntryDao.getJournal(uid = journal.uid)?.copyImpl() ?: JournalEntryDataImpl()))
    suspend fun delete(journal: JournalEntry) = database.journalEntryDao.delete(journal = journal.asData(database.journalEntryDao.getJournal(uid = journal.uid)?.copyImpl() ?: JournalEntryDataImpl()))
    
    
    private val defaultMoods = listOf(
        Mood(emoji = "):<", uid = "default-mood-0"),
        Mood(emoji = "D:", uid = "default-mood-2"),
        Mood(emoji = "):", uid = "default-mood-4"),
        Mood(emoji = ":)", uid = "default-mood-6"),
        Mood(emoji = ":D", uid = "default-mood-8"),
    )
    
    suspend fun getMood(uid: String): Mood = defaultMoods.find { it.uid == uid }!!
    fun queryMoods(): Flow<List<Mood>> = MutableStateFlow(defaultMoods)
}