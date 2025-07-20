package de.julianschwers.diary.feature.journal

import de.julianschwers.diary.core.model.JournalEntry

interface JournalEditorState {
    data object Loading : JournalEditorState
    data class Editor(val entry: JournalEntry) : JournalEditorState
}