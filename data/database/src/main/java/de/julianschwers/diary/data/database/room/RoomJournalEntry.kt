package de.julianschwers.diary.data.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.julianschwers.diary.data.database.JournalEntryData
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity
data class RoomJournalEntry @OptIn(ExperimentalTime::class) constructor(
    override var text: String? = null,
    override var moodUid: String? = null,
    override var timeMillis: Long = Clock.System.now().toEpochMilliseconds(),
    
    override var createdMillis: Long = Clock.System.now().toEpochMilliseconds(),
    @PrimaryKey override var uid: String,
) : JournalEntryData