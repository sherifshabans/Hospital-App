package com.elsharif.hospitalapp.test.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.elsharif.hospitalapp.dataofchecklist.Question
import com.elsharif.hospitalapp.test.data.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(question: Question)

/*
    @Delete
    suspend fun deleteNote(note: Note)
*/

    @Query("SELECT * FROM Question ORDER BY dateAdded")
    fun getNotes(): Flow<List<Question>>


    /*

        @Query("SELECT * FROM note ORDER BY title ASC")
        fun getNotesOrderdByTitle(): Flow<List<Note>>
    */

}











