package com.elsharif.hospitalapp.dataofchecklist

import androidx.compose.foundation.MutatePriority
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
@TypeConverters(ListConverter::class)
data class Question(
    var checkList: String,
    var items: List<Item>,
    var name:String,
    var hospital:String,
    var sub:String,
    var priority: Int,
    var dateAdded: Long,
    var score:Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
){
    fun formattedDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val date = Date(dateAdded)
        return sdf.format(date)
    }
}

data class Item(
    val title: String,
    var subItems: List<Answer>
)
data class Answer(
    val question: String,
    val answer: String
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