package com.elsharif.hospitalapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.elsharif.hospitalapp.R
import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.dataofchecklist.QuestionDao
import com.elsharif.hospitalapp.test.presentation.QuestionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
package com.elsharif.hospitalapp

import java.io.Serializable


data class CheckList(
    val checkList: String,
    val items: List<CheckListItem>,
  //  var isExpanded: Boolean = false // New property for subitems visibility
): Serializable

data class CheckListItem(
    val title: String,
    val subItems: List<String>
):Serializable
*/
/*

sealed interface NotesEvent {
    //object SortNotes: NotesEvent

    //data class DeleteNote(val note: Note): NotesEvent

    data class SaveNote(
        val question: Question,
    ): NotesEvent
}
class NotesViewModel(private val dao: QuestionDao) : ViewModel() {

    private val isSortedByDateAdded = MutableStateFlow(true)


    private var notes =
        isSortedByDateAdded.flatMapLatest { _ ->
            dao.getAllQuestion()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(
        QuestionState(
            checkList = mutableStateOf(""),
            items = mutableStateOf(emptyList()),
            questionTitle = mutableStateOf(""),
            subItems = mutableStateOf(emptyList()),
            question = mutableStateOf(""),
            answer = mutableStateOf(0)

        )
    )

    val state =
        combine(_state, isSortedByDateAdded, notes) { state, isSortedByDateAdded, notes ->
            state.copy(
                notes = notes
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestionState())

    fun onEvent(event: com.elsharif.hospitalapp.test.presentation.NotesEvent) {
        when (event) {


            is com.elsharif.hospitalapp.test.presentation.NotesEvent.SaveNote -> {

                */
/* val answer= Answer(
                     question= state.value.question.value.toString(),
                     answer = state.value.answer.value
                 )

                 val items= Item(
                     title = state.value.questionTitle.value,
                     subItems = listOf(answer)
                 )
                     items = listOf(items),
                 *//*

                val question = Question(
                    checkList = state.value.checkList.value,
                    items=state.value.items.value,
                    dateAdded = System.currentTimeMillis(),
                )


                viewModelScope.launch {
                    dao.upsertQuestion(question)
                }

                _state.update {
                    it.copy(
                        checkList = mutableStateOf(""),
                        items = mutableStateOf(emptyList()),
                        questionTitle = mutableStateOf(""),
                        subItems = mutableStateOf(emptyList()),
                        question = mutableStateOf(""),
                        answer = mutableStateOf(0)

                    )
                }
            }

            */
/*NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
            *//*

        }
    }
}

data class QuestionState(
    val notes: List<Question> = emptyList(),
    val checkList: MutableState<String> = mutableStateOf(""),
    val items: MutableState<List<Item>> = mutableStateOf(emptyList()),
    val questionTitle: MutableState<String> = mutableStateOf(""),
    val subItems: MutableState<List<Answer>> = mutableStateOf(emptyList()),
    val question: MutableState<String> = mutableStateOf(""),
    val answer: MutableState<Int> = mutableStateOf(0),
    val selectedOptions: MutableMap<String, Int> = mutableMapOf()


)

@Entity
@TypeConverters(ListConverter::class)
data class Question(
    val checkList: String,
    val items: List<Item>,
    val dateAdded: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

data class Item(
    val title: String,
    val subItems: List<Answer>
)
data class Answer(
    val question: String,
    val answer: Int
)


class ListConverter {
    @TypeConverter
    fun fromString(value: String): List<Item> {
        val listType = object : TypeToken<List<Item>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<Item>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        onClick = {}

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

            */
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
       *//*

        }
    }
}
*/
