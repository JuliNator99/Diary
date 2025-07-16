package de.julianschwers.diary.data.database

import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface JournalEntryData {
    var text: String?
    var moodUid: String?
    var timeMillis: Long
    
    var createdMillis: Long
    val uid: String
}