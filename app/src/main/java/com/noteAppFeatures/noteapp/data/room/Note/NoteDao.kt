package com.noteAppFeatures.noteapp.data.room.Note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert()
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table WHERE id =:id")
    suspend fun getNoteByName(id: Long): Note?

    @Query("SELECT * FROM note_table")
    fun getAllItems(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE title =:searchText")
    fun getAllSearchItems(searchText: String): Flow<List<Note>>

}