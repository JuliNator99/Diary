package de.julianschwers.diary.core.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Mood @OptIn(ExperimentalUuidApi::class) constructor(
    val emoji: String = "",
    
    val uid: String = Uuid.random().toString(),
)