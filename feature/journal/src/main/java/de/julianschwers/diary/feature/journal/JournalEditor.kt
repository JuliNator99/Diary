package de.julianschwers.diary.feature.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.model.Mood
import de.julianschwers.diary.core.theme.ThemeLayer
import de.julianschwers.diary.core.theme.padding
import de.julianschwers.diary.core.util.getDisplayName
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
internal fun JournalEditor(
    state: JournalEditorState,
    onDiscard: () -> Unit,
    onSave: () -> Unit,
    change: (JournalEntry) -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            onDiscard = onDiscard,
            onSave = onSave,
        )
    }) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.large),
            contentPadding = PaddingValues(MaterialTheme.padding.large),
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                DateTimeSelector(time = state.entry.time)
            }
            item {
                MoodSelector(
                    selected = state.allMoods.find { it.uid == state.entry.moodUid },
                    allMoods = state.allMoods,
                    onSelect = { change(state.entry.copy(moodUid = it?.uid)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TextField(
                    text = state.entry.text,
                    onTextChange = { change(state.entry.copy(text = it)) })
            }
        }
    }
}


@Composable
private fun TextField(
    text: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
        ) {
            Icon(imageVector = Icons.Outlined.Description, contentDescription = null)
            Text(text = stringResource(R.string.note_info))
        }
        Surface(shape = MaterialTheme.shapes.medium) {
            val state = rememberTextFieldState(initialText = text)
            
            LaunchedEffect(Unit) { snapshotFlow { state.text }.collect { onTextChange(it.toString()) } }
            
            val style = MaterialTheme.typography.bodyLarge
            BasicTextField(
                state = state,
                textStyle = style,
                decorator = { innerField ->
                    Box(modifier = Modifier.padding(MaterialTheme.padding.small)) {
                        innerField()
                        
                        if (state.text.isEmpty()) Text(text = stringResource(R.string.note_placeholder), style = style, modifier = Modifier.alpha(0.8f))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun DateTimeSelector(
    time: Instant,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = {}) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
            ) {
                Icon(imageVector = Icons.Rounded.DateRange, contentDescription = null)
                Text(text = time.getDisplayName(dateStyle = FormatStyle.FULL))
            }
        }
        TextButton(onClick = {}) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
            ) {
                Icon(imageVector = Icons.Rounded.AccessTime, contentDescription = null)
                Text(text = time.getDisplayName(timeStyle = FormatStyle.SHORT))
            }
        }
    }
}

@Composable
private fun MoodSelector(
    selected: Mood?,
    allMoods: List<Mood>,
    modifier: Modifier = Modifier,
    onSelect: (Mood?) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        allMoods.forEach { mood ->
            SelectableMood(
                mood = mood,
                selected = mood == selected,
                onToggleSelect = { if (mood == selected) onSelect(null) else onSelect(mood) },
            )
        }
    }
}

@Composable
private fun SelectableMood(
    mood: Mood,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onToggleSelect: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
        modifier = modifier.clickable(
            onClick = onToggleSelect,
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )
    ) {
        Text(
            text = mood.emoji,
            style = MaterialTheme.typography.titleMedium
        )
        RadioButton(
            selected = selected,
            onClick = onToggleSelect
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onDiscard: () -> Unit,
    onSave: () -> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = { IconButton(onClick = onDiscard) { Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null) } },
        actions = {
            TextButton(onClick = onSave) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
                ) {
                    Icon(imageVector = Icons.Outlined.Save, contentDescription = null)
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    )
}


@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun JournalEditorPreview() {
    ThemeLayer {
        var journal by remember { mutableStateOf(JournalEntry()) }
        val moods = remember {
            mutableStateListOf(
                Mood(emoji = "):<"),
                Mood(emoji = "D:"),
                Mood(emoji = "):"),
                Mood(emoji = ":)"),
                Mood(emoji = ":D"),
            )
        }
        
        JournalEditor(
            state = JournalEditorState(
                entry = journal,
                allMoods = moods
            ),
            onDiscard = {},
            onSave = {},
            change = { journal = it })
    }
}