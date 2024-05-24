package com.elsharif.hospitalapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elsharif.hospitalapp.dataofchecklist.QuestionDao
import com.elsharif.hospitalapp.dataofchecklist.QuestionDatabase
import com.elsharif.hospitalapp.presentation.CheckListScreen
import com.elsharif.hospitalapp.test.presentation.AddNoteScreen
import com.elsharif.hospitalapp.test.presentation.DefultNotesScreen
import com.elsharif.hospitalapp.test.presentation.NotesScreenContent
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
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @SuppressLint("Range")
    private fun checkExistingColumns(database: SupportSQLiteDatabase): Set<String> {
        val existingColumns = mutableSetOf<String>()
        database.query("PRAGMA table_info(`Question`)").use {
            while (it.moveToNext()) {
                existingColumns.add(it.getString(it.getColumnIndex("name")))
            }
        }
        return existingColumns
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val existingColumns = checkExistingColumns(database)

            if (!existingColumns.contains("name")) {
                database.execSQL("ALTER TABLE Question ADD COLUMN name TEXT")
            }

            if (!existingColumns.contains("hospital")) {
                database.execSQL("ALTER TABLE Question ADD COLUMN hospital TEXT")
            }

            if (!existingColumns.contains("answer")) {
                database.execSQL("ALTER TABLE Question ADD COLUMN answer TEXT")
            }
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val existingColumns = checkExistingColumns(database)

            if (!existingColumns.contains("score")) {
                database.execSQL("ALTER TABLE Question ADD COLUMN score REAL NOT NULL DEFAULT 0.0")
            }
        }
    }


    private val viewModel by viewModels<NotesViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(database.dao, applicationContext ) as T
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
                val stateDefult by viewModel.stateDefult.collectAsState()
                val stateCompete by viewModel.stateCompete.collectAsState()
                val navController = rememberNavController()

                val loginStatus = viewModel.checkLoginStatus()

                NavHost(navController= navController, startDestination = if (loginStatus) "HomeScreen" else "LoginScreen") {

                    composable("notes") {
                        // Pass the necessary parameters to the NotesScreen composable
                        NotesScreen(
                            state = state,
                            stateDefult=stateDefult,
                            stateComplete = stateCompete,
                            navController = navController,
                            viewModel = viewModel, // Initialize your NotesViewModel here
                            onEvent = viewModel::onEvent
                        )
                    }
                    composable("HomeScreen") {
                        // Pass the necessary parameters to the NotesScreen composable
                        MainScreen(
                            navController = navController,
                            viewModel = viewModel, // Initialize your NotesViewModel here
                            )
                    }
                    composable("DefultNotesScreen") {
                        DefultNotesScreen(
                            state = stateDefult,
                            navController = navController,
                            viewModel=viewModel,
                            onEvent = viewModel::onEvent
                            )
                    }
                    composable("NotesScreen") {
                        NotesScreenContent(
                            state = stateCompete,
                            navController = navController,
                            viewModel=viewModel,
                            onEvent = viewModel::onEvent
                            )
                    }
                    composable("LoginScreen") {
                        LoginScreen(
                            navController = navController,
                              viewModel=viewModel
                        )
                    }
                    ///{argument2}/{argument3}/{argument4}
                    composable("CheckListScreen/{argument}/{argument2}/{argument3}/{argument4}") {navBackStackEntry->
                        val argument = navBackStackEntry.arguments?.getString("argument")
                        val argument2 = navBackStackEntry.arguments?.getString("argument2")
                        val argument3 = navBackStackEntry.arguments?.getString("argument3")
                        val argument4 = navBackStackEntry.arguments?.getString("argument4")

                        CheckListScreen(
                            state = state,
                            navController = navController,
                            onEvent = viewModel::onEvent,
                            viewModel=viewModel,
                            context = context,
                            argument = argument,
                            argument2 = argument2,
                            argument3 = argument3,
                            argument4 =argument4

                        )
                    }
                    composable("Start") {
                        StartScreen(
                            navController = navController
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
