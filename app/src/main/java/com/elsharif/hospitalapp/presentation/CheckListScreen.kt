package com.elsharif.hospitalapp.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elsharif.hospitalapp.CheckData
import com.elsharif.hospitalapp.CheckList
import com.elsharif.hospitalapp.CheckListItem
import com.elsharif.hospitalapp.dataofchecklist.Answer
import com.elsharif.hospitalapp.dataofchecklist.Item
import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.test.presentation.NotesEvent
import com.elsharif.hospitalapp.test.presentation.QuestionState

/*data class Question(
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
val answer: String
)
* */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "AutoboxingStateCreation")
@Composable
fun CheckListScreen(
    state: QuestionState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit,
    context: Context,
    argument: String?
) {
    val checkLists = CheckData.getCheckListByName(argument!!)
    val subitems = remember { mutableStateListOf<Item>() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val question = Question(
                    checkList = state.checkList.value,
                    items = state.items.value,
                    dateAdded = System.currentTimeMillis()
                )
                onEvent(NotesEvent.SaveNote(question))
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save Note"
                )
            }
        }
    ) {
        Column {
            if (checkLists == null) {
                Text(text = "Not Found")
            } else {
                LazyColumn {
                    items(checkLists.items) { checkListItem ->
                        var isExpanded by remember { mutableStateOf(false) }
                        var selectedOptions by remember { mutableStateOf(List(checkListItem.subItems.size) { -1 }) }
                        val answers = remember { mutableStateListOf<Answer>() }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Title: ${checkListItem.title}",
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { isExpanded = !isExpanded }) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            if (isExpanded) {
                                checkListItem.subItems.forEachIndexed { index, subItem ->
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(4.dp)
                                    ) {
                                        Text(text = "${index + 1} : $subItem", maxLines = 2)
                                        Spacer(modifier = Modifier.height(8.dp))

                                        RadioButtonGroup(selectedOptions[index]) { selectedOption ->
                                            selectedOptions = selectedOptions.toMutableList().apply {
                                                set(index, selectedOption)
                                            }
                                            val existingAnswerIndex = answers.indexOfFirst { it.question == subItem }
                                            if (existingAnswerIndex != -1) {
                                                answers[existingAnswerIndex] = Answer(subItem, selectedOption)
                                            } else {
                                                answers.add(Answer(subItem, selectedOption))
                                            }

                                        }
                                    }
                                }
                                // here i'm solve the problem of duplication
                                subitems.clear()
                                subitems.add(Item(checkLists.checkList, answers))
                                state.items.value = subitems

                            }
                            val sum = selectedOptions.filter { it != -1 }.sum()
                            Text(text = "Total Sum: $sum", fontWeight = FontWeight.Bold)
                        }
                    }
                    state.checkList.value = checkLists.checkList
                }
            }
        }
    }
}
@Composable
fun RadioButtonGroup(selectedOption: Int, onSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp)
    ) {
        listOf(0, 1, 2).forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onSelected(option) },
                    colors = RadioButtonDefaults.colors(
                        MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = option.toString(),
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
