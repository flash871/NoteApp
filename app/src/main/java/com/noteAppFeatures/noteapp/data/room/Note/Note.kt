package com.noteAppFeatures.noteapp.data.room.Note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = NAME_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String,
    val description: String
)
const val NAME_TABLE ="note_table"


