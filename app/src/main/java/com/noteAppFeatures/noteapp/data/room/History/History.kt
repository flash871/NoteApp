package com.noteAppFeatures.noteapp.data.room.History

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = HISTORY_TABLE_NAME)
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val storyText: String
)

const val HISTORY_TABLE_NAME = "history_table"