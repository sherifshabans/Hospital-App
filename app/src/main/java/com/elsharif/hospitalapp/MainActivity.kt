package com.elsharif.hospitalapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.elsharif.hospitalapp.dataofchecklist.QuestionDao
import com.elsharif.hospitalapp.dataofchecklist.QuestionDatabase
import com.elsharif.hospitalapp.presentation.CheckListScreen
import com.elsharif.hospitalapp.test.presentation.AddNoteScreen
import com.elsharif.hospitalapp.test.presentation.NotesScreen
import com.elsharif.hospitalapp.test.presentation.NotesViewModel

import com.elsharif.hospitalapp.ui.theme.HospitalAppTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class CheckList(val checkList: String, val items: List<CheckListItem>)

@Serializable
data class CheckListItem(val title: String, val subItems: List<String>)

class MainActivity : ComponentActivity() {


    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            QuestionDatabase::class.java,
            "question.db"
        ).build()
    }

    private val viewModel by viewModels<NotesViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(database.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            HospitalAppTheme {

                val context = LocalContext.current

                 CheckData.initList(context)

               val state by viewModel.state.collectAsState()
                val navController = rememberNavController()

                NavHost(navController= navController, startDestination = "NotesScreen") {
                    composable("NotesScreen") {
                        NotesScreen(
                            state = state,
                            navController = navController,
                        )
                    }
                    composable("CheckListScreen/{argument}") {navBackStackEntry->
                        val argument = navBackStackEntry.arguments?.getString("argument")

                        CheckListScreen(
                            state = state,
                            navController = navController,
                            onEvent = viewModel::onEvent,
                            viewModel=viewModel,
                            context = context,
                            argument = argument

                        )
                    }
                    composable("Start") {
                        StartScreen(
                            navController = navController
                            /*    state = state,
                                checkLists = checkListItems,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            */
                        )
                    }
                    composable("update/{argument}") {navBackStackEntry->
                        val argument = navBackStackEntry.arguments?.getString("argument")

                        UpdateScreen(
                        state = state,
                        navController = navController,
                        viewModel= viewModel,
                        argument=argument,
                        onEvent = viewModel::onEvent
                        )
                    }
                }


          //      CheckListScreen(checkListItems)
            }
        }
    }
}
