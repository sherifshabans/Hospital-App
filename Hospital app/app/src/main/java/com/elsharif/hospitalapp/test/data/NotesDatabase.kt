package com.elsharif.hospitalapp.test.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.dataofchecklist.QuestionDao


@Database(
    entities = [Note::class, Question::class],
    version = 1,
    exportSchema = false
)
abstract class NotesDatabase: RoomDatabase(){
    abstract val dao: NoteDao
    abstract val questiondao: QuestionDao
}