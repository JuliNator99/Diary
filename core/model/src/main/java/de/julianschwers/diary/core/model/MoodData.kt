package de.julianschwers.diary.core.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class Mood {
    VERY_LOW,
    LOW,
    MODERATE,
    HIGH,
    VERY_HIGH
}

data class MoodData @OptIn(ExperimentalUuidApi::class) constructor(
    val emoji: String = "",
    
    val uid: String = Uuid.random().toString(),
)