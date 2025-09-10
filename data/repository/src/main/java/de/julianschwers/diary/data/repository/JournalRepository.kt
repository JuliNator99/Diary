package de.julianschwers.diary.data.repository

import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.data.database.AttachmentsDatabase
import de.julianschwers.diary.data.database.Database
import de.julianschwers.diary.data.database.JournalEntryDataImpl
import de.julianschwers.diary.data.database.copyImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.InputStream
import java.io.Reader
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class JournalRepository(
    private val database: Database,
    private val attachments: AttachmentsDatabase,
) {
    suspend fun getJournal(uid: String): JournalEntry? = database.journalEntryDao.getJournal(uid = uid)?.asModel()
    fun queryJournals(): Flow<List<JournalEntry>> = database.journalEntryDao.queryJournals().map { flow -> flow.map { it.asModel() } }
    
    suspend fun upsert(journal: JournalEntry) = database.journalEntryDao.upsert(journal = journal.asData(database.journalEntryDao.getJournal(uid = journal.uid)?.copyImpl() ?: JournalEntryDataImpl()))
    suspend fun delete(journal: JournalEntry) = database.journalEntryDao.delete(journal = journal.asData(database.journalEntryDao.getJournal(uid = journal.uid)?.copyImpl() ?: JournalEntryDataImpl()))
    
    @OptIn(ExperimentalUuidApi::class)
    fun saveAttachment(attachment: InputStream): String {
        val name = Uuid.random().toString()
        attachments.write(name = name, data = attachment)
        
        return name
    }
    
    fun getAttachments(journal: JournalEntry): List<File> = journal.attachments.mapNotNull { if (attachments.exists(it)) attachments.read(it) else null }
}