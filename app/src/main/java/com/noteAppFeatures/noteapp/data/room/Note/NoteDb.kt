package com.noteAppFeatures.noteapp.data.room.Note

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDb() : RoomDatabase() {

    abstract val dao: NoteDao

    companion object {
        fun createDatabase(context: Context): NoteDb {
            return Room.databaseBuilder(
                context,
                NoteDb::class.java,
                NAME_TABLE
            ).build()
        }

    }
}