package com.noteAppFeatures.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.noteAppFeatures.noteapp.data.NoteViewModel
import com.noteAppFeatures.noteapp.data.room.History.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLine(
    noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory),
    historyViewModel: HistoryViewModel = viewModel(factory = HistoryViewModel.factory)
) {
    var text = noteViewModel.searchText.value
    val noteSearchList = noteViewModel.noteList.collectAsState(initial = emptyList())
    val historyList = historyViewModel.historyList.collectAsState(initial = emptyList())
    val isActiveSearch = noteViewModel.isActiveSearch
    val currentSearchList = noteSearchList.value.filter {
        it.title.lowercase().contains(noteViewModel.searchText.value.lowercase())
    }
    if (!isActiveSearch.value) {
        noteViewModel.searchText.value = ""
    }
    SearchBar(
        query = noteViewModel.searchText.value,
        onQueryChange = {
            noteViewModel.searchText.value = it
            noteViewModel.isShowSearchHistory.value = true
        },
        onSearch = {
            historyViewModel.insertHistoryItem(noteViewModel.searchText.value)
            noteViewModel.isShowSearchHistory.value = false
        },
        active = noteViewModel.isActiveSearch.value,
        onActiveChange = { noteViewModel.isActiveSearch.value = it },
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        trailingIcon = {
            if (noteViewModel.searchText.value.isNotEmpty()) {
                IconButton(onClick = { noteViewModel.searchText.value = "" }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear icon")
                }
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_padding))
    ) {

        if (noteViewModel.searchText.value.isEmpty() || currentSearchList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = dimensionResource(id = R.dimen.medium_padding))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.not_found),
                        contentDescription = "not found image",
                        modifier = Modifier
                            .size(100.dp)

                    )
                    Text(text = stringResource(id = R.string.notSearch), fontSize = 20.sp)
                }

            }
        } else {
            if (noteViewModel.isShowSearchHistory.value) {
                HistoryList()
            }


            LazyColumn(
                contentPadding = PaddingValues(dimensionResource(R.dimen.small_padding))
            ) {
                items(currentSearchList) { noteItem ->
                    NoteItemList(
                        note = noteItem,
                        title = noteItem.title,
                        description = noteItem.description,
                        onClickDelete = {
                            noteViewModel.temporaryAlertDialogNote = noteItem
                            noteViewModel.isShowingAlertDialog.value = true
                        },
                        onNoteCardClick = {
                            noteViewModel.openNoteCard(noteItem)
                            noteViewModel.noteItem = it
                        }
                    )

                }
            }
        }
    }
}


