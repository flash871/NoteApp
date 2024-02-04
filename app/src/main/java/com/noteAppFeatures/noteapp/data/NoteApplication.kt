package com.noteAppFeatures.noteapp.data

import android.app.Application
import com.noteAppFeatures.noteapp.data.room.History.HistoryDb
import com.noteAppFeatures.noteapp.data.room.Note.NoteDb

class NoteApplication : Application() {
    val database by lazy { NoteDb.createDatabase(this) }
    val historyDatabase by lazy { HistoryDb.createHistoryDatabase(this) }
}