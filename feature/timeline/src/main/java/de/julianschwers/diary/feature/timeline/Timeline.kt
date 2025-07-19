package de.julianschwers.diary.feature.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.model.Mood
import de.julianschwers.diary.core.theme.ThemeLayer
import de.julianschwers.diary.core.theme.padding
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@Composable
private fun TimelineScreen(
    state: TimelineState,
    modifier: Modifier = Modifier,
    onCreateNew: () -> Unit,
    onDelete: (JournalEntry) -> Unit,
    onEdit: (JournalEntry) -> Unit,
) {
    if (state !is TimelineState.Timeline) return
    
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onCreateNew) { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) }
    }) { innerPadding ->
        Timeline(
            entries = state.entries,
            onDelete = onDelete,
            onEdit = onEdit,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun Timeline(
    entries: Map<LocalDate, List<JournalEntry>>,
    modifier: Modifier = Modifier,
    onDelete: (JournalEntry) -> Unit,
    onEdit: (JournalEntry) -> Unit,
) {
    val entriesByDate = remember(entries) { entries.entries.sortedByDescending { it.key }.map { it.value } }
    
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
        contentPadding = PaddingValues(MaterialTheme.padding.medium),
        modifier = modifier
    ) {
        items(items = entriesByDate) { dateEntries ->
            JournalDay(
                journals = dateEntries,
                onDelete = onDelete,
                onEdit = onEdit,
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun TimelinePreview() {
    ThemeLayer {
        val journals = remember {
            listOf(
                JournalEntry(text = "What is going on with this.", time = Clock.System.now() - 0.123.days, mood = Mood(emoji = ":D")),
                JournalEntry(text = "What is going on with this.", mood = Mood(emoji = ":D")),
                JournalEntry(text = "What is going on with this.", time = Clock.System.now() - 1.123.days, mood = Mood(emoji = ":D"))
            )
        }
        
        Timeline(
            entries = journals.groupBy { it.time.toLocalDateTime(TimeZone.currentSystemDefault()).date },
            onDelete = {},
            onEdit = {},
        )
    }
}