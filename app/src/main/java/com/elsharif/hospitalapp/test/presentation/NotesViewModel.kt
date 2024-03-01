package com.elsharif.hospitalapp.test.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elsharif.hospitalapp.CheckListItem
import com.elsharif.hospitalapp.dataofchecklist.Answer
import com.elsharif.hospitalapp.dataofchecklist.Item

import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.dataofchecklist.QuestionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(private val dao: QuestionDao) : ViewModel() {

    private val isSortedByDateAdded = MutableStateFlow(true)
    var selectedOptions: MutableMap<String, List<Int>> = mutableMapOf()

    private val _one = MutableStateFlow(0)
    val one = _one.asStateFlow()

    private val _zero = MutableStateFlow(0)
    val zero = _zero.asStateFlow()

    private val _two=MutableStateFlow(0)
    var two=_two.asStateFlow()

    fun onIncreaseZero() {

        _zero.update {
            it+1
        }
    }
    fun onIncreaseOne(){

        _one.update {
         it+1
       }
    }
    fun onIncreaseTwo(){
     _two.update {
         it+1
     }
    }
    fun resetValues() {
        _one.value = 0
        _zero.value = 0
        _two.value = 0
    }

    private val _myList= MutableStateFlow(listOf(0,1,2))
    var myList =_myList.asStateFlow()



    private val _answersMap = mutableMapOf<CheckListItem, MutableList<Answer>>()
    val answersMap: Map<CheckListItem, List<Answer>> get() = _answersMap

    private val _selectedOptionsMap = mutableMapOf<CheckListItem, List<Int>>()
    var selectedOptionsMap: MutableStateFlow<Map<CheckListItem, List<Int>>> = MutableStateFlow(_selectedOptionsMap)
    private val _selectedOptionsMapState = MutableStateFlow(_selectedOptionsMap.toMap())
    val selectedOptionsMapState: StateFlow<Map<CheckListItem, List<Int>>> get() = _selectedOptionsMapState

    fun resetSelectedOptionsMap() {
        _selectedOptionsMap.clear()
        _selectedOptionsMapState.value = _selectedOptionsMap.toMap()
    }
    fun getSelectedOptions(checkListItem: CheckListItem): List<Int> {
        return _selectedOptionsMap.getOrPut(checkListItem) { List(checkListItem.subItems.size) { -1 } }
    }

  /*  fun updateSelectedOption(checkListItem: CheckListItem, index: Int, selectedOption: Int) {
        val selectedOptions = _selectedOptionsMap.getOrPut(checkListItem) { MutableList(checkListItem.subItems.size) { -1 } }
            .toMutableList()
        selectedOptions[index] = selectedOption
        _selectedOptionsMap[checkListItem] = selectedOptions.toList() // Ensure immutability
        selectedOptionsMap.value = _selectedOptionsMap.toMap() // Notify observers
    }
*/

  fun updateSelectedOption(checkListItem: CheckListItem, index: Int, selectedOption: Int) {
      _selectedOptionsMap[checkListItem] = _selectedOptionsMap[checkListItem]?.toMutableList()?.apply {
          this[index] = selectedOption
      } ?: List(checkListItem.subItems.size) { -1 }
      _selectedOptionsMapState.value = _selectedOptionsMap.toMap()
    }
    fun getAnswers(checkListItem: CheckListItem): List<Answer> {
        return _answersMap.getOrPut(checkListItem) { mutableListOf() }
    }

    fun updateAnswer(checkListItem: CheckListItem, answer: Answer) {
        val answers = _answersMap.getOrPut(checkListItem) { mutableListOf() }
        val existingAnswerIndex = answers.indexOfFirst { it.question == answer.question }
        if (existingAnswerIndex != -1) {
            answers[existingAnswerIndex] = answer
        } else {
            answers.add(answer)
        }

/*
        for(ans in answers){
            if(ans.answer==0){
                onIncreaseZero()
            }
            else if(ans.answer==1){
                onIncreaseOne()
            }
            else if(ans.answer==2){
                onIncreaseTwo()
            }
        }
*/
    }

    // for Update

    private val _answersMap2 = mutableMapOf<Item, MutableList<Answer>>()
    val answersMap2: Map<Item, List<Answer>> get() = _answersMap2

    private val _selectedOptionsMap2 = mutableMapOf<Item, List<Int>>()
    var selectedOptionsMap2: MutableStateFlow<Map<Item, List<Int>>> = MutableStateFlow(_selectedOptionsMap2)
    private val _selectedOptionsMapState2 = MutableStateFlow(_selectedOptionsMap2.toMap())
    val selectedOptionsMapState2: StateFlow<Map<Item, List<Int>>> get() = _selectedOptionsMapState2


    fun updateSelectedOption(checkListItem: Item, index: Int, selectedOption: Int) {
        _selectedOptionsMap2[checkListItem] = _selectedOptionsMap2[checkListItem]?.toMutableList()?.apply {
            this[index] = selectedOption
        } ?: List(checkListItem.subItems.size) { -1 }
        _selectedOptionsMapState2.value = _selectedOptionsMap2.toMap()
    }

    fun getAnswers(checkListItem: Item): List<Answer> {
        return _answersMap2.getOrPut(checkListItem) { mutableListOf() }
    }


    fun updateAnswer(checkListItem: Item, answer: Answer) {
        val answers = _answersMap2.getOrPut(checkListItem) { mutableListOf() }
        val existingAnswerIndex = answers.indexOfFirst { it.question == answer.question }
        if (existingAnswerIndex != -1) {
            answers[existingAnswerIndex] = answer
        } else {
            answers.add(answer)
        }
    }


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
    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            dao.upsertQuestion(question)
        }
    }
}
