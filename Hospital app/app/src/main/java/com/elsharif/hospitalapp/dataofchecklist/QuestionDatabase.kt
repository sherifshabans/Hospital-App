package com.elsharif.hospitalapp.dataofchecklist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Question::class], // Add any new entities or updated entities
    version = 3,
    exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class QuestionDatabase : RoomDatabase() {

    abstract val dao: QuestionDao

}
