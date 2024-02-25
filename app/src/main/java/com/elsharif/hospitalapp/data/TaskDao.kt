package com.elsharif.hospitalapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {


    @Upsert
    suspend fun upsertTask(task: Task){}

    @Delete
    suspend fun deleteTask(task: Task){}

    @Query("SELECT *FROM task ")
     fun getAllTasks():Flow<List<Task>>


}