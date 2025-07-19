package de.julianschwers.diary.feature.timeline

import de.julianschwers.diary.core.model.JournalEntry
import kotlinx.datetime.LocalDate

sealed interface TimelineState {
    data object Loading : TimelineState
    data class Timeline(val entries: Map<LocalDate, List<JournalEntry>>) : TimelineState
}