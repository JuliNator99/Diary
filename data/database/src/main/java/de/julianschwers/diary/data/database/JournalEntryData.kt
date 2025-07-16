package de.julianschwers.diary.data.database

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
interface JournalEntryData {
    var text: String?
    var moodUid: String?
    var timeMillis: Long
    
    var createdMillis: Long
    var uid: String
}

fun JournalEntryData.copyImpl(): JournalEntryDataImpl =
    JournalEntryDataImpl(
        text = text,
        moodUid = moodUid,
        timeMillis = timeMillis,
        createdMillis = createdMillis,
        uid = uid
    )

data class JournalEntryDataImpl @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class) constructor(
    override var text: String? = null,
    override var moodUid: String? = null,
    override var timeMillis: Long = Clock.System.now().toEpochMilliseconds(),
    
    override var createdMillis: Long = Clock.System.now().toEpochMilliseconds(),
    override var uid: String = Uuid.random().toString(),
) : JournalEntryData