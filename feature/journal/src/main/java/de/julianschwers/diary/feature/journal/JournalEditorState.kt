package de.julianschwers.diary.feature.journal

import de.julianschwers.diary.core.model.JournalEntry
import java.io.InputStream

interface JournalEditorState {
    data object Loading : JournalEditorState
    data class Editor(
        val entry: JournalEntry,
        val attachments: List<InputStream> = emptyList(),
    ) : JournalEditorState
}