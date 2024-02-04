package com.noteAppFeatures.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.noteAppFeatures.noteapp.data.NoteViewModel
import com.noteAppFeatures.noteapp.data.room.Note.Note


@Composable
fun NoteApp(noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory)) {
    val isShowingDetail = noteViewModel.isShowingDetailScreen.value
    if (isShowingDetail) {
        DetailScreen()
    } else {
        noteViewModel.descriptionText.value = ""
        noteViewModel.titleText.value = ""
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    NoteTopAppBar()
    SearchLine()
    NoteList()
}

@Composable
fun NoteItemList(
    note: Note,
    title: String,
    description: String,
    onClickDelete: () -> Unit,
    onNoteCardClick: (Note) -> Unit,

    ) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                dimensionResource(id = R.dimen.small_padding)
            )
            .clickable { onNoteCardClick(note) }
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()

        ) {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1, fontSize = 25.sp, modifier = Modifier.padding(
                    dimensionResource(
                        id = R.dimen.medium_padding
                    )
                )

            )
            Text(
                text = description,
                overflow = TextOverflow.Ellipsis, maxLines = 3,
                modifier = Modifier.padding(
                    start =
                    dimensionResource(
                        id = R.dimen.medium_padding
                    ),
                    end = dimensionResource(
                        id = R.dimen.medium_padding
                    ),
                    bottom = dimensionResource(
                        id = R.dimen.medium_padding
                    )
                )
            )

        }

        IconButton(
            onClick = { onClickDelete() },
            modifier = Modifier.align(Alignment.End)
        )
        {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "null"
            )


        }
    }
}

@SuppressLint(
    "SuspiciousIndentation", "StateFlowValueCalledInComposition",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun NoteList(
    noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory),

    ) {

    val noteList = noteViewModel.noteList.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { noteViewModel.isShowingDetailScreen.value = true },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.Yellow,

                ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(dimensionResource(R.dimen.small_padding))
        )
        {
            items(noteList.value.reversed()) { noteItem ->
                NoteItemList(
                    title = noteItem.title,
                    description = noteItem.description,
                    note = noteItem,
                    onClickDelete = {
                        noteViewModel.temporaryAlertDialogNote = noteItem
                        noteViewModel.isShowingAlertDialog.value = true
                    },
                    onNoteCardClick = {
                        noteViewModel.openNoteCard(noteItem)
                        noteViewModel.noteItem = it
                    }
                )
                if (noteViewModel.isShowingAlertDialog.value) {
                    WarningDeleteAlertDialog(
                        onDeleteNote = {
                            noteViewModel.temporaryAlertDialogNote?.let { it1 ->
                                noteViewModel.deleteNoteItem(
                                    it1
                                )
                            }
                            noteViewModel.isShowingAlertDialog.value = false
                        },
                        note = noteItem,
                        dialogState = noteViewModel.isShowingAlertDialog
                    )
                }

            }

        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteTopAppBar(
    noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory)
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (noteViewModel.isActiveSearch.value) {
                    IconButton(onClick = { noteViewModel.isActiveSearch.value = false }) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = null,
                            modifier = Modifier.padding(
                                end = dimensionResource(id = R.dimen.small_padding),
                                top = dimensionResource(id = R.dimen.medium_padding)
                            )
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.noteName),
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.medium_padding)),
                )
            }
        }
    )
}


@Composable
fun WarningDeleteAlertDialog(
    noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory),
    onDeleteNote: (Note?) -> Unit,
    note: Note? = null,
    dialogState: MutableState<Boolean>


) {
    AlertDialog(
        title = { },
        text = {
            Text(
                text = stringResource(id = R.string.contentDialog) +
                        " " + noteViewModel.temporaryAlertDialogNote?.title,
                overflow = TextOverflow.Ellipsis
            )
        },
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteNote(noteViewModel.temporaryAlertDialogNote)
                }) {
                Text(text = stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dialogState.value = false
                }) {
                Text(text = stringResource(id = R.string.back))
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun NoteItemListPreview(
    noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory)
) {

}


