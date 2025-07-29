package de.julianschwers.diary.feature.journal

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.julianschwers.diary.core.common.getDisplayName
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.model.Mood
import de.julianschwers.diary.core.theme.ThemeLayer
import de.julianschwers.diary.core.theme.padding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.io.InputStream
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
internal fun JournalEditor(
    state: JournalEditorState.Editor,
    onDiscard: () -> Unit,
    onSave: () -> Unit,
    attach: (List<InputStream>) -> Unit,
    change: (JournalEntry) -> Unit,
) {
    val moodColor by animateColorAsState(state.entry.mood?.let { colorResource(LocalMoods.current.getColor(it)) } ?: MaterialTheme.colorScheme.primary)
    
    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(primary = moodColor)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    onDiscard = onDiscard,
                    onSave = onSave,
                )
            },
            bottomBar = { BottomAppBar(onAttach = attach) },
            modifier = Modifier.imePadding()
        ) { innerPadding ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.large),
                contentPadding = PaddingValues(MaterialTheme.padding.large),
                modifier = Modifier.padding(innerPadding)
            ) {
                item {
                    DateTimeSelector(
                        time = state.entry.time,
                        onSelect = { change(state.entry.copy(time = it)) },
                    )
                }
                item {
                    MoodSelector(
                        selected = state.entry.mood,
                        onSelect = { change(state.entry.copy(mood = it)) },
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
            
            LaunchedEffect(state, onTextChange) {
                snapshotFlow { state.text }
                    .distinctUntilChanged()
                    .collect { onTextChange(it.toString()) }
            }
            
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

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSelector(
    time: Instant,
    modifier: Modifier = Modifier,
    onSelect: (Instant) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        var showDatePicker by rememberSaveable { mutableStateOf(false) }
        var showTimePicker by rememberSaveable { mutableStateOf(false) }
        
        if (showDatePicker) {
            val dateTime = remember { time.toLocalDateTime(TimeZone.currentSystemDefault()) }
            val state = rememberDatePickerState(initialSelectedDateMillis = dateTime.date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds())
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        
                        val selectedInstant = Instant.fromEpochMilliseconds(state.selectedDateMillis!!)
                        val newDateTime = selectedInstant.toLocalDateTime(TimeZone.UTC).date.atTime(dateTime.time)
                        
                        onSelect(newDateTime.toInstant(TimeZone.currentSystemDefault()))
                    }) { Text(text = stringResource(R.string.ok)) }
                }) {
                DatePicker(state = state)
            }
        }
        if (showTimePicker) {
            val dateTime = remember { time.toLocalDateTime(TimeZone.currentSystemDefault()) }
            val state = rememberTimePickerState(initialHour = dateTime.time.hour, initialMinute = dateTime.time.minute)
            
            DatePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showTimePicker = false
                        
                        val selectedTime = LocalTime(hour = state.hour, minute = state.minute)
                        val newDateTime = selectedTime.atDate(dateTime.date)
                        
                        onSelect(newDateTime.toInstant(TimeZone.currentSystemDefault()))
                    }) { Text(text = stringResource(R.string.ok)) }
                }) {
                TimePicker(
                    state = state,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        TextButton(onClick = { showDatePicker = true }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
            ) {
                Icon(imageVector = Icons.Rounded.DateRange, contentDescription = null)
                Text(text = time.getDisplayName(dateStyle = FormatStyle.FULL))
            }
        }
        TextButton(onClick = { showTimePicker = true }) {
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
    modifier: Modifier = Modifier,
    onSelect: (Mood?) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        Mood.entries.forEach { mood ->
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
            text = LocalMoods.current.getEmoji(mood),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomAppBar(
    onAttach: (List<InputStream>) -> Unit,
) {
    val context = LocalContext.current
    val mediaPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        val inputStreams = uris.map { context.contentResolver.openInputStream(it)!! }
        onAttach(inputStreams)
    }
    
    BottomAppBar {
        IconButton(onClick = { mediaPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)) }) { Icon(imageVector = Icons.Outlined.AttachFile, contentDescription = null) }
    }
}


@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun JournalEditorPreview() {
    ThemeLayer {
        var journal by remember { mutableStateOf(JournalEntry()) }
        
        JournalEditor(
            state = JournalEditorState.Editor(entry = journal),
            onDiscard = {},
            onSave = {},
            attach = {},
            change = { journal = it })
    }
}