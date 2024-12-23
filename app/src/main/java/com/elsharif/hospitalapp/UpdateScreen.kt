package com.elsharif.hospitalapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elsharif.hospitalapp.CheckData
import com.elsharif.hospitalapp.CheckList
import com.elsharif.hospitalapp.CheckListItem
import com.elsharif.hospitalapp.dataofchecklist.Answer
import com.elsharif.hospitalapp.dataofchecklist.Item
import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.presentation.RadioButtonGroup
import com.elsharif.hospitalapp.test.presentation.NotesEvent
import com.elsharif.hospitalapp.test.presentation.NotesViewModel
import com.elsharif.hospitalapp.test.presentation.QuestionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpdateScreen(
    state: QuestionState,
    navController: NavController,
    viewModel: NotesViewModel,
     onEvent: (NotesEvent) -> Unit,
    argument:String?

) {

    val subitems = remember { mutableStateListOf<Item>() }
    var isExpanded by remember { mutableStateOf(false) }
    //val selectedOptionsMap by viewModel.selectedOptionsMap.collectAsState()



    val selectedOptionsMapState by viewModel.selectedOptionsMapState2.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            // Reset data in ViewModel when navigating away
            viewModel.resetValues()
           viewModel.resetSelectedOptionsMap() // Reset the selectedOptionsMap when the screen is disposed

        }
    }


    // convert the argument to int to get the id
   // Assuming argument is a String representing the id
    val questionId = argument?.toIntOrNull() ?: -1
    var question by remember { mutableStateOf<Question?>(null) }


     LaunchedEffect(questionId) {
        if (questionId != -1) {
            viewModel.getQuestionById(questionId).collect { fetchedQuestion ->
                if (fetchedQuestion != null) {
                    Log.d("ViewModel", "Fetched question: $fetchedQuestion")
                    question = fetchedQuestion
                } else {
                    Log.e("ViewModel", "Failed to fetch question for id: $questionId")
                }
            }
        }
    }


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
                    text = stringResource(id = R.string.update_task),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },


        floatingActionButton = {


            FloatingActionButton(onClick = {


                if(question!!.priority==1)
                    question!!.priority=1

                question?.let { updatedQuestion ->
                    viewModel.updateQuestion(updatedQuestion)
                }
                navController.popBackStack()

            }) {

                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save Note"
                )

            }
        }
    ) {
        Column (
            modifier=Modifier.fillMaxSize().padding(it)
        ){
            if (question == null) {
                Text(text = "Not Found")
            } else {

                LazyColumn {
                    items(question!!.items) { checkListItem ->


                        val selectedOptions = selectedOptionsMapState[checkListItem] ?: List(checkListItem.subItems.size) { "" }
                        Log.i("selectedOptions","selectedOptions :$selectedOptions")
                         var isExpanded by remember { mutableStateOf(false) }


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
                                IconButton(onClick = { isExpanded = !isExpanded }) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                }
                                Text(
                                    text = " ${checkListItem.title}",
                                    fontWeight = FontWeight.Bold
                                )



                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            if (isExpanded) {


                                checkListItem.subItems.forEachIndexed { index, subItem ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp)

                                    ) {
                                        Text(text = "${index + 1} : ${subItem.question}")
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Radio buttons for choices
                                        RadioButtonGroup(selectedOptions[index],viewModel ,checkListItem,index, subItem.answer) { selectedOption ->
                                            viewModel.updateAnswer(checkListItem, Answer(subItem.question, selectedOption))
                                        }
                                    }

                                }



                            }

                        }


                    }
                    if(question!!.priority==1){
                        question!!.priority=1
                    }
                    question!!.checkList = question!!.checkList
                    question!!.dateAdded=question!!.dateAdded
                    question!!.hospital=question!!.hospital
                    question!!.name=question!!.name
                    question!!.sub=question!!.sub
                    question!!.score=question!!.score

                }
            }
        }



    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RadioButtonGroup(selectedOption: String,viewModel:NotesViewModel, checkListItem :Item, index:Int, answer: String ,onSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {

        viewModel.myList.collectAsState().value.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {

                RadioButton(
                    selected = selectedOption == option ||answer==option,
                    onClick = {
                        viewModel.updateSelectedOption(checkListItem, index, option)
                        onSelected(option)
                    }
                    ,
                    colors = RadioButtonDefaults.colors(
                        MaterialTheme.colorScheme.primary
                    ),
                )
                Text(
                    text = option.toString(),
                    modifier = Modifier.padding(4.dp)
                )

            }
        }
    }
}
