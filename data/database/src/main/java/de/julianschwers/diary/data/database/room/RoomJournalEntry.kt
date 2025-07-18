package de.julianschwers.diary.data.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.julianschwers.diary.data.database.JournalEntryData
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class RoomJournalEntry @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class) constructor(
    override var text: String? = null,
    override var moodUid: String? = null,
    override var timeMillis: Long = Clock.System.now().toEpochMilliseconds(),
    
    override var createdMillis: Long = Clock.System.now().toEpochMilliseconds(),
    @PrimaryKey override var uid: String = Uuid.random().toString(),
) : JournalEntryData