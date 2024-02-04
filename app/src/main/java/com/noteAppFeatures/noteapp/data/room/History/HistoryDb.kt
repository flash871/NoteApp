package com.noteAppFeatures.noteapp.data.room.History

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class HistoryDb : RoomDatabase() {

    abstract val historyDao: HistoryDao

    companion object {
        fun createHistoryDatabase(context: Context): HistoryDb {
            return Room.databaseBuilder(
                context,
                HistoryDb::class.java,
                HISTORY_TABLE_NAME
            ).build()
        }
    }
}