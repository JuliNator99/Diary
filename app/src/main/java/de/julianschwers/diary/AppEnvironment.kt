package de.julianschwers.diary

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.julianschwers.diary.data.database.AttachmentsDatabase
import de.julianschwers.diary.data.database.directory.DirectoryAttachmentsDatabase
import de.julianschwers.diary.data.database.room.RoomDatabase
import de.julianschwers.diary.data.repository.JournalRepository
import de.julianschwers.diary.data.repository.MoodRepository
import java.io.File
import kotlin.time.TimeSource

private const val TAG = "AppEnvironment"

class AppEnvironment(private val context: Context) {
    var isInitialising: Boolean by mutableStateOf(false)
        private set
    var isDatabaseLoaded: Boolean by mutableStateOf(false)
        private set
    
    lateinit var roomDatabase: RoomDatabase
    lateinit var attachmentsDatabase: AttachmentsDatabase
    lateinit var journalRepository: JournalRepository
    lateinit var moodRepository: MoodRepository
    
    init {
        initialise()
    }
    
    fun initialise() {
        if (isInitialising || isDatabaseLoaded) throw IllegalStateException("AppEnvironment can not be initialised. Environment is either currently initialising ($isInitialising) or already initialised ($isDatabaseLoaded).")
        isInitialising = true
        Log.i(TAG, "Initialising App Environment...")
        val start = TimeSource.Monotonic.markNow()
        
        initAttachments()
        initRoom()
        
        Log.i(TAG, "App Environment successfully initialised in ${start.elapsedNow()}!")
        isDatabaseLoaded = true
        isInitialising = false
    }
    
    private fun initRoom() {
        Log.i(TAG, "Initialising Room Database...")
        val start = TimeSource.Monotonic.markNow()
        
        val database = RoomDatabase(context = context)
        
        roomDatabase = database
        
        initRoomRepos()
        
        Log.i(TAG, "Room Database successfully initialised in ${start.elapsedNow()}!")
    }
    
    private fun initAttachments() {
        Log.i(TAG, "Initialising Attachments...")
        val start = TimeSource.Monotonic.markNow()
        
        val database = DirectoryAttachmentsDatabase(directory = File(context.dataDir, "attachments"))
        
        attachmentsDatabase = database
        
        initRoomRepos()
        
        Log.i(TAG, "Attachments successfully initialised in ${start.elapsedNow()}!")
    }
    
    private fun initRoomRepos() {
        val repo = JournalRepository(database = roomDatabase)
        journalRepository = repo
        moodRepository = MoodRepository()
    }
}