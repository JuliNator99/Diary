package de.julianschwers.diary.feature.journal

import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.model.Mood

interface JournalEditorState {
    data object Loading : JournalEditorState
    data class Editor(
        val entry: JournalEntry,
        val allMoods: List<Mood>,
    ) : JournalEditorState
}