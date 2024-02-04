package com.noteAppFeatures.noteapp.data.room.History

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.noteAppFeatures.noteapp.data.NoteApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HistoryViewModel(val historyDatabase: HistoryDb) : ViewModel() {

    val historyList = historyDatabase.historyDao.getAllHistoryItems()
    var historyItem: History? = null


    fun insertHistoryItem(textSearch: String) = viewModelScope.launch {
        val currentHistoryItem = historyItem?.copy(storyText = textSearch) ?: History(
            storyText = textSearch
        )
        if (!checkStoryTextExists(historyDatabase, textSearch)) {
            historyDatabase.historyDao.insertHistory(currentHistoryItem)
            historyItem = null
        }


    }

    suspend fun checkStoryTextExists(database: HistoryDb, text: String): Boolean {
        var idExists = false

        // Запуск асинхронного запроса в базу данных
        withContext(Dispatchers.IO) {
            val item = database.historyDao.getHistoryByStoryText(text)
            idExists = item != null
        }

        return idExists
    }

    fun deleteHistoryItem(history: History) = viewModelScope.launch {
        historyDatabase.historyDao.deleteHistory(history)
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database =
                    (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)).historyDatabase
                return HistoryViewModel(database) as T
            }
        }
    }
}