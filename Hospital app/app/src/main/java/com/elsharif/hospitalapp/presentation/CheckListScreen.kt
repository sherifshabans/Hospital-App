package com.elsharif.hospitalapp.presentation

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
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elsharif.hospitalapp.CheckData
import com.elsharif.hospitalapp.CheckListItem
import com.elsharif.hospitalapp.R
import com.elsharif.hospitalapp.dataofchecklist.Answer
import com.elsharif.hospitalapp.dataofchecklist.Item
import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.test.presentation.NotesEvent
import com.elsharif.hospitalapp.test.presentation.NotesViewModel
import com.elsharif.hospitalapp.test.presentation.QuestionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "AutoboxingStateCreation", "StateFlowValueCalledInComposition")
@Composable
fun CheckListScreen(
    state: QuestionState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit,
    viewModel: NotesViewModel,
    context: Context,
    argument: String?,
    argument2: String?,
    argument3: String?,
    argument4: String?
) {
    val checkLists = CheckData.getCheckListByName(argument!!)


    val selectedOptionsMapState by viewModel.selectedOptionsMapState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            // Reset data in ViewModel when navigating away
            viewModel.resetValues()
            viewModel.resetSelectedOptionsMap()
        // Reset the selectedOptionsMap when the screen is disposed

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
                    text = stringResource(id = R.string.add_task),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },

        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
        ,

        floatingActionButton = {

            FloatingActionButton(onClick = {
                state.hospital.value=argument3.toString()
                state.name.value=argument2.toString()
                state.sub.value=argument4.toString()

                var countZero = 0
                var countOne = 0
                var countTwo = 0
                var countNa = 0

                state.items.value.forEach {checkLists->
                    checkLists.subItems.forEachIndexed { index, subItem ->
                        when (subItem.answer) {
                            "0" -> countZero++
                            "1" -> countOne++
                            "2" -> countTwo++
                            "N/A" -> countNa++
                        }


                    }
                }

                viewModel.onIncreaseZero(countZero)
                viewModel.onIncreaseOne(countOne)
                viewModel.onIncreaseTwo(countTwo)
                viewModel.onIncreaseNa(countNa)

                val sum = countOne + countTwo*2
                val num = (countOne+countTwo+countZero)*2
                val score = if (num == 0) 0.0 else sum.toDouble() / num.toDouble()

                state.score.value=score.toDouble()
          Log.i("the all ","$countZero  , $countOne ,  $countTwo  , $countNa")
                val question = Question(
                    checkList = state.checkList.value,
                    items = state.items.value,
                    priority = state.priority.value,
                    name = state.name.value,
                    sub = state.sub.value,
                    score = state.score.value,
                    hospital = state.hospital.value,
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
        Column(
            modifier= Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (checkLists == null) {
                Text(text = "Not Found")
            } else {

                LazyColumn {


                    items(checkLists.items) { checkListItem ->


                        val selectedOptions = selectedOptionsMapState[checkListItem] ?: List(checkListItem.subItems.size) { "" }
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
                                        Text(text = "${index + 1} : $subItem")
                                        Spacer(modifier = Modifier.height(8.dp))

                                        RadioButtonGroup(selectedOptions[index],viewModel ,checkListItem,index) { selectedOption ->
                                                viewModel.updateAnswer(checkListItem, Answer(subItem, selectedOption))
                                            Log.d("Composable", "Selected option for $checkListItem: $selectedOption")
                                            Log.d("Composable", "Updated answer for $checkListItem: ${viewModel.getAnswers(checkListItem)}")
                                        }

                                    }
                                }


                                // Update state.items.value after iterating over all subitems

                                val updatedItems = checkLists.items.map { item ->
                                    // Get the existing answers for the item
                                    val existingAnswers = viewModel.getAnswers(item)

                                    val updatedAnswers = item.subItems.map { subItem ->
                                        // Check if the subItem's question is already in the existing answers
                                        val existingAnswer = existingAnswers.find { it.question == subItem }
                                        if (existingAnswer == null) {
                                            // If not found, create a new Answer with an empty string
                                            Answer(subItem, "")
                                        } else {
                                            // If found, use the existing answer
                                            existingAnswer
                                        }
                                    }

                                    Item( item.title, updatedAnswers)
                                }
                                state.items.value = updatedItems

                            }

                        }

                    }
                    state.checkList.value = checkLists.checkList
                }


            }

        }



    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RadioButtonGroup(selectedOption: String,viewModel:NotesViewModel, checkListItem :CheckListItem, index:Int ,onSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        viewModel.myList.value.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {

                RadioButton(
                    selected = selectedOption == option,
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
