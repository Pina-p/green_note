package com.techniquesmyanmar.greennotes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.techniquesmyanmar.greennotes.entity.Notes

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE notes.user_id == :id ORDER BY id DESC")
    fun getAllNotes(id:Int) : LiveData<List<Notes>>

    @Query("SELECT * FROM notes WHERE id =:id")
    fun getSpecificNote(id:Int) : LiveData<Notes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes)

    @Delete
     suspend fun deleteNote(note:Notes)

    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun deleteSpecificNote(id:Int)

    @Query("DELETE FROM notes WHERE user_id = :id")
    suspend fun deleteUserData(id:Int)

   // @Update
    //suspend fun updateNote(note:Notes)

    @Query("""
        SELECT * 
        FROM Notes
        where LOWER(title) LIKE '%' || LOWER(:query) || '%' OR
                LOWER(noteText) LIKE '%' || LOWER(:query) || '%' OR
                LOWER(dateTime) LIKE '%' || LOWER(:query) || '%' 
                 
    """)
    fun searchNoteListing(query:String): LiveData<List<Notes>>
}