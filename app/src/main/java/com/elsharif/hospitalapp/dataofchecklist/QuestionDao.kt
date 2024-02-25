package com.elsharif.hospitalapp.dataofchecklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {


    @Upsert
    suspend fun upsertQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT *FROM Question ")
     fun getAllQuestion():Flow<List<Question>>

    @Query("SELECT * FROM Question WHERE id = :questionId")
    fun getQuestionById(questionId: Int): Flow<Question?>



}