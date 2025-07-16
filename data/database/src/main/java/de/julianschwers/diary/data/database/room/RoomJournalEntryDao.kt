package de.julianschwers.diary.data.database.room

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import de.julianschwers.diary.data.database.JournalEntryDao
import de.julianschwers.diary.data.database.JournalEntryData
import kotlinx.coroutines.flow.Flow

interface RoomJournalEntryDao : JournalEntryDao {
    @Query(
        """
    SELECT * FROM RoomJournalEntry
    WHERE uid = :uid
"""
    )
    override fun getJournal(uid: String): JournalEntryData
    
    @Query(
        """
    SELECT * FROM RoomJournalEntry
"""
    )
    override fun queryJournals(): Flow<List<JournalEntryData>>
    
    
    @Upsert
    override fun upsert(journal: JournalEntryData)
    
    @Delete
    override fun delete(journal: JournalEntryData)
}