package com.elsharif.hospitalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.elsharif.hospitalapp.test.presentation.DefultNotesScreen
import com.elsharif.hospitalapp.test.presentation.NotesEvent
import com.elsharif.hospitalapp.test.presentation.NotesScreenContent
import com.elsharif.hospitalapp.test.presentation.NotesViewModel
import com.elsharif.hospitalapp.test.presentation.QuestionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    state: QuestionState,
    stateDefult: QuestionState,
    stateComplete: QuestionState,
    navController: NavController,
    viewModel: NotesViewModel,
    onEvent: (NotesEvent) -> Unit
) {
    // Keep track of the current selected tab
    var currentTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            // Your existing top bar
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
        content = {paddingValues->

            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                // Add a TabRow below the top bar
                TabRow(
                    selectedTabIndex = currentTab,
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary).align(Alignment.CenterHorizontally),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[currentTab]),

                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    Tab(
                        selected = currentTab == 0,
                        onClick = {
                            currentTab = 0
                        },
                        text = { Text("مؤكد") }
                    )
                    Tab(
                        selected = currentTab == 1,
                        onClick = {
                            currentTab = 1
                        },
                        text = { Text("مسودة") }
                    )
                }

                // Display the appropriate screen based on the selected tab
                if (currentTab == 0) {

                    NotesScreenContent(stateComplete, navController, viewModel, onEvent)
                } else {
                    DefultNotesScreen(stateDefult, navController, viewModel, onEvent)
                }
            }
        }
    )
}