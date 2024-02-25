package com.elsharif.hospitalapp.dataofchecklist

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [Question::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class QuestionDatabase :RoomDatabase() {

    abstract val dao:QuestionDao
}