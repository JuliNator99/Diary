package de.julianschwers.diary.core.model

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class JournalEntry @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class) constructor(
    val text: String = "",
    val moodUid: String? = null,
    val time: Instant = Clock.System.now(),
    
    val created: Instant = Clock.System.now(),
    val uid: String = Uuid.random().toString(),
)