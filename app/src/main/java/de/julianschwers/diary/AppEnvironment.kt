package de.julianschwers.diary

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.julianschwers.diary.data.database.room.RoomDatabase
import de.julianschwers.diary.data.repository.JournalRepository
import kotlin.time.TimeSource

private const val TAG = "AppEnvironment"

class AppEnvironment(private val context: Context) {
    var isInitialising: Boolean by mutableStateOf(false)
        private set
    var isDatabaseLoaded: Boolean by mutableStateOf(false)
        private set
    
    lateinit var roomDatabase: RoomDatabase
    lateinit var journalRepository: JournalRepository
    
    init {
        initialise()
    }
    
    fun initialise() {
        if (isInitialising || isDatabaseLoaded) throw IllegalStateException("AppEnvironment can not be initialised. Environment is either currently initialising ($isInitialising) or already initialised ($isDatabaseLoaded).")
        isInitialising = true
        Log.i(TAG, "Initialising App Environment...")
        val start = TimeSource.Monotonic.markNow()
        
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
    
    private fun initRoomRepos() {
        val repo = JournalRepository(database = roomDatabase)
        journalRepository = repo
    }
}