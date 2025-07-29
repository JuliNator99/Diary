package de.julianschwers.diary.feature.journal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.data.repository.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream
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
    
    
    val state = journal.map { journal ->
        if (journal == null) return@map JournalEditorState.Loading
        
        JournalEditorState.Editor(entry = journal)
    }
    
    fun update(newJournal: JournalEntry) = journal.update { newJournal }
    fun save(callback: () -> Unit) {
        val journalEntry = journal.value ?: throw NullPointerException("Cannot save while no JournalEntry is loaded.")
        
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsert(journalEntry)
            viewModelScope.launch(Dispatchers.Main) { callback() }
        }
    }
    
    fun attach(items: List<InputStream>) {
        val names = mutableListOf<String>()
        for (item in items) {
            val reader = item.bufferedReader()
            val name = repository.saveAttachment(reader)
            names.add(name)
        }
        
        val currentJournal = journal.value ?: return
        val attachments = currentJournal.attachments
        
        update(
            currentJournal.copy(
                attachments = buildList {
                    addAll(attachments)
                    addAll(names)
                }
            )
        )
    }
}