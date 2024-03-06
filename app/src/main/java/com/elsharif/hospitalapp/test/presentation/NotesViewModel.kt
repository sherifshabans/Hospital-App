package com.elsharif.hospitalapp.test.presentation

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elsharif.hospitalapp.CheckListItem
import com.elsharif.hospitalapp.Login
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

class NotesViewModel(
    private val dao: QuestionDao,
 private val context: Context
) : ViewModel() {

    private val isSortedByDateAdded = MutableStateFlow(true)

    var selectedOptions: MutableMap<String, List<Int>> = mutableMapOf()

    private val _one = MutableStateFlow(0)
    val one = _one.asStateFlow()

    private val _zero = MutableStateFlow(0)
    val zero = _zero.asStateFlow()
    private val _na = MutableStateFlow(0)
    val na = _na.asStateFlow()
    private val _size = MutableStateFlow(0)
    val size = _size.asStateFlow()

    private val _two=MutableStateFlow(0)
    var two=_two.asStateFlow()

    fun onIncreaseZero(newZero:Int) {

        _zero.value=newZero
    }
    fun onSize(new:Int) {

        _size.value=new
    }
    fun onIncreaseNa(newNa:Int) {

        _na.value=newNa
    }
    fun onIncreaseOne(newOne:Int){
          _one.value=newOne

    }
    fun onIncreaseTwo(newTwo:Int){
        _two.value=newTwo
    }
    fun resetValues() {
        _one.value = 0
        _zero.value = 0
        _two.value = 0
    }

    private val _myList= MutableStateFlow(listOf("0","1","2","N/A"))
    var myList =_myList.asStateFlow()



    private val _answersMap = mutableMapOf<CheckListItem, MutableList<Answer>>()
    val answersMap: Map<CheckListItem, List<Answer>> get() = _answersMap

    private val _selectedOptionsMap = mutableMapOf<CheckListItem, List<String>>()
    var selectedOptionsMap: MutableStateFlow<Map<CheckListItem, List<String>>> = MutableStateFlow(_selectedOptionsMap)
    private val _selectedOptionsMapState = MutableStateFlow(_selectedOptionsMap.toMap())
    val selectedOptionsMapState: StateFlow<Map<CheckListItem, List<String>>> get() = _selectedOptionsMapState

    fun resetSelectedOptionsMap() {
        _selectedOptionsMap.clear()
        _selectedOptionsMapState.value = _selectedOptionsMap.toMap()
    }
    fun getSelectedOptions(checkListItem: CheckListItem): List<String> {
        return _selectedOptionsMap.getOrPut(checkListItem) { List(checkListItem.subItems.size) { "-1" } }
    }

   fun updateSelectedOption(checkListItem: CheckListItem, index: Int, selectedOption: String) {
      _selectedOptionsMap[checkListItem] = _selectedOptionsMap[checkListItem]?.toMutableList()?.apply {
          this[index] = selectedOption
      } ?: List(checkListItem.subItems.size) { "" }
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

    }




    // for Update

    private val _answersMap2 = mutableMapOf<Item, MutableList<Answer>>()
    val answersMap2: Map<Item, List<Answer>> get() = _answersMap2

    private val _selectedOptionsMap2 = mutableMapOf<Item, List<String>>()
    var selectedOptionsMap2: MutableStateFlow<Map<Item, List<String>>> = MutableStateFlow(_selectedOptionsMap2)
    private val _selectedOptionsMapState2 = MutableStateFlow(_selectedOptionsMap2.toMap())
    val selectedOptionsMapState2: StateFlow<Map<Item, List<String>>> get() = _selectedOptionsMapState2


    fun updateSelectedOption(checkListItem: Item, index: Int, selectedOption: String) {
        _selectedOptionsMap2[checkListItem] = _selectedOptionsMap2[checkListItem]?.toMutableList()?.apply {
            this[index] = selectedOption
        } ?: List(checkListItem.subItems.size) { "" }
        _selectedOptionsMapState2.value = _selectedOptionsMap2.toMap()
    }

    fun getAnswers(checkListItem: Item): List<Answer> {
        return _answersMap2.getOrPut(checkListItem) { mutableListOf() }
    }


    fun updateAnswer(checkListItem: Item, answer: Answer) {
        val subItems = checkListItem.subItems.toMutableList()
        val existingAnswerIndex = subItems.indexOfFirst { it.question == answer.question }
        if (existingAnswerIndex != -1) {
            subItems[existingAnswerIndex] = answer
        } else {
            subItems.add(answer)
        }
        checkListItem.subItems = subItems


    }
    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            dao.upsertQuestion(question)
        }
    }





    private var notes =
        isSortedByDateAdded.flatMapLatest { _ ->
            dao.getAllQuestion()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private var defultNotes=
        isSortedByDateAdded.flatMapLatest { _ ->
        dao.getDefultQuestion()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var completeNotes=
        isSortedByDateAdded.flatMapLatest { _ ->
        dao.getCompleteQuestion()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(
        QuestionState(
            checkList = mutableStateOf(""),
            items = mutableStateOf(emptyList()),
            priority = mutableStateOf(0),
            questionTitle = mutableStateOf(""),
            name = mutableStateOf(""),
            hospital = mutableStateOf(""),
            subItems = mutableStateOf(emptyList()),
            question = mutableStateOf(""),
            answer = mutableStateOf("")
        )
    )




    val state =
        combine(_state, isSortedByDateAdded, notes) { state, isSortedByDateAdded, notes ->
            state.copy(
                notes = notes,

            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestionState())

    val stateCompete =
        combine(_state, isSortedByDateAdded, completeNotes) { state, isSortedByDateAdded, completeNotes ->
            state.copy(
notes = completeNotes
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestionState())

    val stateDefult =
        combine(_state, isSortedByDateAdded, defultNotes) { state, isSortedByDateAdded, defultNotes ->
            state.copy(
                 notes=defultNotes
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestionState())

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteQuestion(event.question)
                }
            }
            is NotesEvent.SaveNote -> {
                val question = Question(
                    checkList = state.value.checkList.value,
                    items=state.value.items.value,
                    priority = state.value.priority.value,
                    name = state.value.name.value,
                    hospital = state.value.hospital.value,
                    score = state.value.score.value,
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
                        priority = mutableStateOf(0),
                        subItems = mutableStateOf(emptyList()),
                        question = mutableStateOf(""),
                        answer = mutableStateOf(""),
                        name = mutableStateOf(""),
                        hospital = mutableStateOf(""),
                        score = mutableStateOf(0.0),


                    )
                }
            }
            /*NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
            */
            else -> {}
        }
    }
    fun getQuestionById(questionId: Int): Flow<Question?> {
        return dao.getQuestionById(questionId)
    }

    private val hospitals = listOf(
        Login("main hospital", "#main001"),
        Login("student hospital", "#stud002"),
        Login("om el-kosor hospital", "#omel003"),
        Login("traume hospital", "#trau004"),
        Login("new assiut hospital", "#newa005"),
        Login("pediatric hospital", "#pedi006"),
        Login("neurology psychiatry hospital ", "#neps007"),
        Login("urology hospital", "#urol008"),
        Login("woman hospital", "#woma009"),
        Login("cardiology hospital", "#card010"),
        Login("al raghi  hospital", "#alra011")
    )

    fun checkLoginStatus(): Boolean {
        val sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("logged_in", false)
    }

    fun login(username: String, password: String, onLoginSuccess: () -> Unit, onLoginError: () -> Unit) {
        viewModelScope.launch {
            if (hospitals.any { it.name == username && it.password == password }) {
                setLoginStatus(true)
                onLoginSuccess()
            } else {
                onLoginError()
            }
        }
    }

    fun logout() {
        setLoginStatus(false)
    }


    private fun setLoginStatus(status: Boolean) {
        val sharedPreferences =  context.getSharedPreferences("login_status", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("logged_in", status).apply()
    }

    fun setPriorityToOne(question: Question) {
        question.priority = 1
        viewModelScope.launch {
            dao.upsertQuestion(question)
        }
    }
}
