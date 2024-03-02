package com.elsharif.hospitalapp.dataofchecklist

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity
@TypeConverters(ListConverter::class)
data class Question(
    var checkList: String,
    var items: List<Item>,
    val dateAdded: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

data class Item(
    val title: String,
    var subItems: List<Answer>
)
data class Answer(
    val question: String,
    val answer: Int
)


class ListConverter {
    @TypeConverter
    fun fromString(value: String): List<Item> {
        val listType = object : TypeToken<List<Item>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<Item>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}