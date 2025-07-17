package de.julianschwers.diary.feature.journal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.navArgument
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.data.repository.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class JournalEditorVM(
    private val journalUid: String,
    private val repository: JournalRepository,
) : ViewModel() {
    private val tag = "JournalEditorVM-$journalUid"
    val journal: MutableStateFlow<JournalEntry?> = MutableStateFlow(null)
    
    init {
        Log.i(tag, "Initialising JournalEditorVM...")
        
        viewModelScope.launch(Dispatchers.IO) {
            val foundJournal = repository.getJournal(journalUid) ?: navArgRegister.retrieve(journalUid)
            viewModelScope.launch { journal.emit(foundJournal) }
            
            Log.i(tag, "JournalEditorVM initialised using existing Journal (${foundJournal.uid})")
        }
    }
    
    
    val state = journal.combine(repository.queryMoods()) { journal, moods ->
        if (journal == null) return@combine JournalEditorState.Loading
        
        JournalEditorState.Editor(entry = journal, allMoods = moods)
    }
    
    fun update(newJournal: JournalEntry) = journal.update { newJournal }
    fun save(callback: () -> Unit) {
        val journalEntry = journal.value ?: throw NullPointerException("Cannot save while no JournalEntry is loaded.")
        
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsert(journalEntry)
            callback()
        }
    }
}