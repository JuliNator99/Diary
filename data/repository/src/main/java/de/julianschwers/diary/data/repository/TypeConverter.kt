package de.julianschwers.diary.data.repository

import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.data.database.JournalEntryData
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal fun JournalEntryData.asModel(): JournalEntry =
    JournalEntry(
        text = text ?: "",
        moodUid = moodUid,
        time = Instant.fromEpochMilliseconds(timeMillis),
        
        created = Instant.fromEpochMilliseconds(createdMillis),
        uid = uid
    )

@OptIn(ExperimentalTime::class)
internal fun JournalEntry.asData(storedData: JournalEntryData): JournalEntryData =
    storedData.apply {
        this@apply.text = this@asData.text.takeUnless { it.isBlank() }
        this@apply.moodUid = this@asData.moodUid
        this@apply.timeMillis = this@asData.time.toEpochMilliseconds()
        
        this@apply.createdMillis = this@asData.created.toEpochMilliseconds()
        this@apply.uid = this@asData.uid
    }