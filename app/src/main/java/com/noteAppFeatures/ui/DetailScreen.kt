package com.noteAppFeatures.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.noteAppFeatures.noteapp.data.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory)) {

    val titleText = noteViewModel.titleText
    val descriptionText = noteViewModel.descriptionText
    val backgroundTextFieldColor = if (isSystemInDarkTheme()) {
        Color(0xFF354B40)
    } else {

        Color(0xFFCFE9D9)
    }

    noteDetailTopBar()
    Column(modifier = Modifier.fillMaxSize()) {

        TextField(
            value = titleText.value,
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            placeholder = { Text(text = stringResource(id = R.string.name)) },
            onValueChange = { titleText.value = it },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),

                )

        )
        TextField(
            value = descriptionText.value,
            onValueChange = { descriptionText.value = it },
            modifier = Modifier
                .background(backgroundTextFieldColor)
                .fillMaxSize(),
            placeholder = { Text(text = stringResource(id = R.string.writeSomething)) },

            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),

                )

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun noteDetailTopBar(noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.factory)) {

    val titleText = noteViewModel.titleText
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { noteViewModel.isShowingDetailScreen.value = false }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = null,
                        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.medium_padding))
                    )
                }
                Text(
                    text = stringResource(R.string.noteName),
                )
            }
            if (titleText.value.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    IconButton(onClick = { noteViewModel.insertNoteItem() }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "check")
                    }
                }
            }
        },
    )
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen()
}