package de.julianschwers.diary.data.database.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

fun RoomDatabase(context: Context): de.julianschwers.diary.data.database.room.RoomDatabase =
    Room
        .databaseBuilder(context, de.julianschwers.diary.data.database.room.RoomDatabase::class.java, "MainRoom")
        .build()

@Database(
    entities = [
        RoomJournalEntry::class
    ],
    exportSchema = true,
    autoMigrations = [],
    version = 1
)
abstract class RoomDatabase : RoomDatabase(), de.julianschwers.diary.data.database.Database {
    abstract override val journalEntryDao: RoomJournalEntryDao
}