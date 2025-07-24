package de.julianschwers.diary.data.database.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration1to2 : Migration(startVersion = 1, endVersion = 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("UPDATE RoomJournalEntry SET moodUid = 'VERY_LOW' WHERE moodUid = 'default-mood-0'")
        db.execSQL("UPDATE RoomJournalEntry SET moodUid = 'LOW' WHERE moodUid = 'default-mood-2'")
        db.execSQL("UPDATE RoomJournalEntry SET moodUid = 'MODERATE' WHERE moodUid = 'default-mood-4'")
        db.execSQL("UPDATE RoomJournalEntry SET moodUid = 'HIGH' WHERE moodUid = 'default-mood-6'")
        db.execSQL("UPDATE RoomJournalEntry SET moodUid = 'VERY_HIGH' WHERE moodUid = 'default-mood-8'")
    }
}