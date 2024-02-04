package com.noteAppFeatures.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.noteAppFeatures.noteapp.data.NoteViewModel
import com.noteAppFeatures.noteapp.data.room.History.HistoryViewModel


@Composable
fun HistoryCard(
    historyText: String,
    onDeleteHistotyItem: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCardClick()
            },
        shape = RoundedCornerShape(6.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.Refresh, contentDescription = "icon refresh",
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.paddingForHistoryCard))
            )

            Text(
                text = historyText,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.medium_padding),
                    top = dimensionResource(id = R.dimen.medium_padding)
                )
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = { onDeleteHistotyItem() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close icon")
                }
            }

        }


    }
}

@Composable
fun HistoryList(
    historyViewModel: HistoryViewModel = viewModel(factory = HistoryViewModel.factory),
    noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory)
) {
    val historyList = historyViewModel.historyList.collectAsState(initial = emptyList())
    LazyColumn(
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.small_padding)),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(historyList.value.reversed().filter {
            it.storyText.lowercase().startsWith(noteViewModel.searchText.value.lowercase())
        }) { historyItem ->
            HistoryCard(
                historyText = historyItem.storyText,
                onDeleteHistotyItem = { historyViewModel.deleteHistoryItem(historyItem) },
                onCardClick = {
                    noteViewModel.searchText.value = historyItem.storyText
                    noteViewModel.isShowSearchHistory.value = false
                }
            )
        }
    }
}
