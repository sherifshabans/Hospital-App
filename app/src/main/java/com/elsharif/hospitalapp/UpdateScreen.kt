package com.elsharif.hospitalapp

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.elsharif.hospitalapp.test.presentation.NotesViewModel
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpdateScreen(
 //   state: QuestionState,
    navController: NavController,
    viewModel: NotesViewModel,
    // onEvent: (NotesEvent) -> Unit,
    argument:String?

) {

    // convert the argument to int to get the id
    val questionId=argument!!.toInt()
    var question by remember { mutableStateOf<Question?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.getQuestionById(questionId ?: -1).collect { fetchedQuestion ->
            question = fetchedQuestion
        }
    }
   /* val checkLists=CheckData.getCheckListByName(argument!!)
    var items : MutableState<List<Item>> = mutableStateOf(emptyList())
    val answers = mutableListOf<Answer>()
    val subitems = mutableListOf<Item>()
    val questions = mutableListOf<Question>()
   */
    Log.i("State.CheckList ","State : $question")
    question?.let {question->


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {

               /* val question = Question(
                    checkList = state.checkList.value,
                    items = state.items.value,
                    dateAdded = System.currentTimeMillis() // You can replace this with actual date
                )

                onEvent(NotesEvent.SaveNote(question))
               */
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

            var isExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {


                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(question.items) { checkListItem ->

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

                                var selectedOptions by remember {
                                    mutableStateOf(
                                        List(
                                            checkListItem.subItems.size
                                        ) { -1 }
                                    )
                                }

                                var selectedOption by remember { mutableStateOf(0) }

                                checkListItem.subItems.forEachIndexed { index, subItem ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp)

                                    ) {
                                        Text(text = "${index + 1} : ${subItem.question}", maxLines = 2)
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Radio buttons for choices


                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(4.dp)
                                            ) {


                                                RadioButton(
                                                    selected = selectedOptions[index] == 0,
                                                    onClick = {
                                                        selectedOptions =
                                                            selectedOptions.toMutableList()
                                                                .apply {
                                                                    set(index, 0)
                                                                }
                                                        selectedOption = 0

                                                    },
                                                    colors = RadioButtonDefaults.colors(
                                                        MaterialTheme.colorScheme.primary
                                                    ),

                                                    )
                                                Text(
                                                    text = "0",
                                                    modifier = Modifier.padding(4.dp)
                                                )

                                            }

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(4.dp)
                                            ) {
                                                RadioButton(
                                                    selected = selectedOptions[index] ==1,
                                                    onClick = {
                                                        selectedOptions =
                                                            selectedOptions.toMutableList()
                                                                .apply {
                                                                    set(index, 1)
                                                                }
                                                        selectedOption = 1

                                                    },
                                                    colors = RadioButtonDefaults.colors(
                                                        MaterialTheme.colorScheme.primary
                                                    )
                                                )
                                                Text(
                                                    text = "1",
                                                    modifier = Modifier.padding(end = 4.dp)
                                                )

                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(4.dp)
                                            ) {
                                                RadioButton(
                                                    selected = selectedOptions[index] == 2,
                                                    onClick = {
                                                        selectedOption = 2
                                                        selectedOptions =
                                                            selectedOptions.toMutableList()
                                                                .apply { set(index, 2) }
                                                    },
                                                    colors = RadioButtonDefaults.colors(
                                                        MaterialTheme.colorScheme.primary
                                                    )
                                                )
                                                Text(
                                                    text = "2",
                                                    modifier = Modifier.padding(4.dp)
                                                )
                                            }
//                                            answers.add(Answer(subItem, selectedOption))

                                //            Log.d("answers" ,"Answers: ${answers[index]}" )

                                        }


                                    }


                                }

                                //state.subItems.value=answers
                                // Calculate the sum of selected options
                                val sum = selectedOptions.filter { it != -1 }.sum()
                                Text(text = "Total Sum: $sum", fontWeight = FontWeight.Bold)

                            }
                        //    subitems.add(Item(checkListItem.title, answers))

                         //   Log.d("SubItems" ,"SubItems: $subitems" )

                        }



                    }
                }

               // state.items.value=subitems

            }
            //state.checkList.value=checkLists.checkList


            // question.add(Question(checkLists.checkList,subitems,System.currentTimeMillis()))
        }
    }
}}
