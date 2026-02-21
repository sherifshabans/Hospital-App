package com.elsharif.hospitalapp.test.presentation

import com.elsharif.hospitalapp.dataofchecklist.Item
import com.elsharif.hospitalapp.dataofchecklist.Answer
import com.elsharif.hospitalapp.dataofchecklist.Question

sealed interface NotesEvent {
    //object SortNotes: NotesEvent

    data class DeleteNote(val question: Question): NotesEvent

    data class SaveNote(
        val question: Question,
    ): NotesEvent
}
