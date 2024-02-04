package com.noteAppFeatures.noteapp.data.room.History

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Delete
    suspend fun deleteHistory(history: History)

    @Query("SELECT * FROM history_table")
    fun getAllHistoryItems(): Flow<List<History>>

    @Query("SELECT * FROM history_table WHERE storyText =:searchText")
    fun getHistoryByStoryText(searchText: String): History?
}