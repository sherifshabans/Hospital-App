package com.elsharif.hospitalapp.test.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elsharif.hospitalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    state: QuestionState,
    navController: NavController,
) {
    Log.i("NotesScreen", "state.notes: ${state.notes}")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )


            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.checkList.value = ""
                state.items .value = emptyList()
                state.subItems.value = emptyList()
                state.questionTitle.value = ""
                state.question.value=""
                state.answer.value=0
                navController.navigate("Start")
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add new note")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(state.notes.size) { index ->
                NoteItem(
                    state = state,
                    index = index,
                    navController = navController
                )
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    state: QuestionState,
    index: Int,
    navController: NavController,

    ) {
   Log.i("NoteItem", "itemcheck: ${state.notes[0].checkList}")

    val id = state.notes[index].id
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        onClick = {
            navController.navigate("update/$id")
        }

    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(12.dp),

            ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {


                val itemcheck = if (state.notes[index].checkList.isNotEmpty()) {
                    state.notes[index].checkList
                } else {
                    "Not Found"
                }
                Text(
                    text = itemcheck,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )


            }

            /*   IconButton(
                   onClick = {
                       onEvent(NotesEvent.DeleteNote(state.notes[index]))
                   }
               ) {

                   Icon(
                       imageVector = Icons.Rounded.Delete,
                       contentDescription = "Delete Note",
                       modifier = Modifier.size(35.dp),
                       tint = MaterialTheme.colorScheme.onPrimaryContainer
                   )

               }
       */
        }
    }
}













