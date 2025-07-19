package de.julianschwers.diary.feature.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.model.Mood
import de.julianschwers.diary.core.theme.ThemeLayer
import de.julianschwers.diary.core.theme.card
import de.julianschwers.diary.core.theme.elevation
import de.julianschwers.diary.core.theme.padding
import de.julianschwers.diary.core.util.getDisplayName
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
internal fun JournalDay(
    journals: List<JournalEntry>,
    modifier: Modifier = Modifier,
    onDelete: (JournalEntry) -> Unit,
    onEdit: (JournalEntry) -> Unit,
) {
    val sortedEntries = remember(journals) { journals.sortedByDescending { it.time } }
    
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.large,
        shadowElevation = MaterialTheme.elevation.card,
        modifier = modifier
    ) {
        Column {
            Surface(color = MaterialTheme.colorScheme.primaryContainer) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = journals.first().time.getDisplayName(dateStyle = FormatStyle.FULL),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = MaterialTheme.padding.small,
                                horizontal = MaterialTheme.padding.medium
                            )
                    )
                }
            }
            
            sortedEntries.forEach { journalEntry ->
                JournalItem(
                    journalEntry = journalEntry,
                    addConnectingLine = sortedEntries.last() != journalEntry,
                    onDelete = { onDelete(journalEntry) },
                    onEdit = { onEdit(journalEntry) },
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun JournalItem(
    journalEntry: JournalEntry,
    addConnectingLine: Boolean,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    Surface(
        color = Color.Transparent,
        onClick = onEdit,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
            modifier = Modifier
                .padding(MaterialTheme.padding.medium)
                .height(IntrinsicSize.Min)
        ) {
            Sidebar(
                mood = journalEntry.mood,
                line = addConnectingLine,
            )
            MainInfo(
                text = journalEntry.text,
                time = journalEntry.time,
                onDelete = onDelete,
                onEdit = onEdit
            )
        }
    }
}


@OptIn(ExperimentalTime::class)
@Composable
private fun MainInfo(
    text: String,
    time: Instant,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        modifier = modifier
    ) {
        InfoBar(
            time = time,
            onDelete = onDelete,
            onEdit = onEdit
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Sidebar(
    mood: Mood?,
    line: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = mood?.emoji ?: "",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.minimumInteractiveComponentSize()
        )
        if (line) {
            VerticalDivider(
                thickness = 3.dp,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }
}


@OptIn(ExperimentalTime::class)
@Composable
private fun InfoBar(
    time: Instant,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = time.getDisplayName(timeStyle = FormatStyle.SHORT),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        IconButton(onClick = { showDropdown = true }) {
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false },
                shape = MaterialTheme.shapes.medium
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.edit)) },
                    onClick = {
                        onEdit()
                        showDropdown = false
                    },
                    leadingIcon = { Icon(imageVector = Icons.Outlined.Edit, contentDescription = null) },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.delete)) },
                    onClick = {
                        onDelete()
                        showDropdown = false
                    },
                    leadingIcon = { Icon(imageVector = Icons.Outlined.Delete, contentDescription = null) },
                )
            }
            
            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
        }
    }
}


@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun JournalDayPreview() {
    ThemeLayer {
        val journals = remember {
            listOf(
                JournalEntry(text = "What is going on with this. May If I make this longer some day it appears", mood = Mood(emoji = ":D")),
                JournalEntry(text = "What is going on with this. May If I make this longer some day it appears. May If I make this longer some day it appears. May If I make this longer some day it appears. May If I make this longer some day it appears", mood = Mood(emoji = ":D")),
                JournalEntry(text = "What is going on with this.", mood = Mood(emoji = ":D"))
            )
        }
        
        JournalDay(
            journals = journals,
            onDelete = {},
            onEdit = {},
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun JournalItemPreview() {
    ThemeLayer {
        val journal = remember { JournalEntry(text = "What is going on with this.", mood = Mood(emoji = ":D")) }
        
        JournalItem(
            journalEntry = journal,
            addConnectingLine = true,
            onDelete = {},
            onEdit = {},
        )
    }
}