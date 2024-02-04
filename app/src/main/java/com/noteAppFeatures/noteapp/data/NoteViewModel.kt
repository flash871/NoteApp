package com.noteAppFeatures.noteapp.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.noteAppFeatures.noteapp.data.room.Note.Note
import com.noteAppFeatures.noteapp.data.room.Note.NoteDb
import kotlinx.coroutines.launch

class NoteViewModel(val database: NoteDb) : ViewModel() {

    val noteList = database.dao.getAllItems()
    val titleText = mutableStateOf("")
    val descriptionText = mutableStateOf("")
    val searchText = mutableStateOf("")
    var noteItem: Note? = null
    var temporaryAlertDialogNote: Note? = null


    var isShowingDetailScreen = mutableStateOf(false)
    var isActiveSearch = mutableStateOf(false)
    val isShowingAlertDialog = mutableStateOf(false)
    val isShowSearchHistory = mutableStateOf(true)


    fun insertNoteItem() = viewModelScope.launch {
        val inserCurrenttItem =
            noteItem?.copy(title = titleText.value, description = descriptionText.value)
                ?: Note(title = titleText.value, description = descriptionText.value)
        // to make noteItem the first in database
        if (noteItem != null) {
            val tmpNoteUpdateTitle = inserCurrenttItem.title
            val tmpDescription = inserCurrenttItem.description
            database.dao.deleteNote(inserCurrenttItem)
            database.dao.insertNote(Note(title = tmpNoteUpdateTitle, description = tmpDescription))
            isShowingDetailScreen.value = false
            noteItem = null
        } else {
            database.dao.insertNote(inserCurrenttItem)
            isShowingDetailScreen.value = false
            noteItem = null
        }
    }

    fun deleteNoteItem(note: Note) = viewModelScope.launch {
        database.dao.deleteNote(note)
    }

    fun openNoteCard(note: Note) = viewModelScope.launch {
        note.id?.let {
            database.dao.getNoteByName(it)
            isShowingDetailScreen.value = true
            titleText.value = note.title
            descriptionText.value = note.description
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY] as NoteApplication)).database
                return NoteViewModel(database) as T
            }
        }
    }

}

