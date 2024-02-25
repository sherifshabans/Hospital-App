package com.elsharif.hospitalapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elsharif.hospitalapp.dataofchecklist.Question

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val university:String,
    val hospital :String,
    val department:String,
    val subDepartment:String,
    val checkList:String,
    val listDate:String,
    val listTime:String,
  //  val list:List<Question>,
    val auditorCode:String,
    val visit :String,
    val isNA :Boolean
)
