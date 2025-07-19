package de.julianschwers.diary.feature.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.data.repository.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class TimelineVM(
    private val repository: JournalRepository,
) : ViewModel() {
    @OptIn(ExperimentalTime::class)
    val state = repository
        .queryJournals()
        .map { journals ->
            TimelineState.Timeline(entries = journals.groupBy {
                it.time.toLocalDateTime(TimeZone.currentSystemDefault()).date
            })
        }.stateIn(
            scope = viewModelScope + Dispatchers.IO,
            started = SharingStarted.Eagerly,
            initialValue = TimelineState.Loading
        )
    
    fun delete(entry: JournalEntry) {
        viewModelScope.launch(Dispatchers.IO) { repository.delete(entry) }
    }
}