package com.elsharif.hospitalapp.test.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elsharif.hospitalapp.dataofchecklist.Answer
import com.elsharif.hospitalapp.dataofchecklist.Item

import com.elsharif.hospitalapp.test.data.NoteDao
import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.dataofchecklist.QuestionDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.SaveNote -> {
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
            /*NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
            */
        }
    }
    fun getQuestionById(questionId: Int): Flow<Question?> {
        return dao.getQuestionById(questionId)
    }
}
