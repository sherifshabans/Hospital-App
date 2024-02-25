package com.elsharif.hospitalapp.test.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.elsharif.hospitalapp.test.data.Note
import com.elsharif.hospitalapp.dataofchecklist.Answer
import com.elsharif.hospitalapp.dataofchecklist.Item
import com.elsharif.hospitalapp.dataofchecklist.Question

data class QuestionState(
    val notes: List<Question> = emptyList(),
    val checkList: MutableState<String> = mutableStateOf(""),
    val items: MutableState<List<Item>> = mutableStateOf(emptyList()),
    val questionTitle: MutableState<String> = mutableStateOf(""),
    val subItems: MutableState<List<Answer>> = mutableStateOf(emptyList()),
    val question: MutableState<String> = mutableStateOf(""),
    val answer: MutableState<Int> = mutableStateOf(0),
    val selectedOptions: MutableMap<String, Int> = mutableMapOf()


)
