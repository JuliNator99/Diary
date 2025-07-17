package de.julianschwers.diary.data.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import de.julianschwers.diary.data.database.JournalEntryDao
import de.julianschwers.diary.data.database.JournalEntryData
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomJournalEntryDao : JournalEntryDao {
    @Query(
        """
    SELECT * FROM RoomJournalEntry
    WHERE uid = :uid
"""
    )
    override suspend fun getJournal(uid: String): RoomJournalEntry?
    
    @Query(
        """
    SELECT * FROM RoomJournalEntry
"""
    )
    override fun queryJournals(): Flow<List<RoomJournalEntry>>
    
    
    @Upsert
    suspend fun internalUpsert(journal: RoomJournalEntry)
    
    @Delete
    suspend fun internalDelete(journal: RoomJournalEntry)
    
    override suspend fun upsert(journal: JournalEntryData) {
        val stored = getJournal(uid = journal.uid)?.copy() ?: RoomJournalEntry() // TODO this does not update anything
        internalUpsert(stored)
    }
    
    override suspend fun delete(journal: JournalEntryData) {
        val stored = getJournal(uid = journal.uid)?.copy() ?: return
        internalDelete(stored)
    }
}