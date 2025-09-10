package de.julianschwers.diary.feature.journal

import de.julianschwers.diary.core.model.JournalEntry
import java.io.File

interface JournalEditorState {
    data object Loading : JournalEditorState
    data class Editor(
        val entry: JournalEntry,
        val attachments: List<File> = emptyList(),
    ) : JournalEditorState
}