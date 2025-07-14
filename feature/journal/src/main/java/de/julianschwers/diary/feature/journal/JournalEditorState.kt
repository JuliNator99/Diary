package de.julianschwers.diary.feature.journal

import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.model.Mood

data class JournalEditorState(
    val entry: JournalEntry,
    val allMoods: List<Mood>,
)