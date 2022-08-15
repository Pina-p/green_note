package com.techniquesmyanmar.greennotes.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.techniquesmyanmar.greennotes.AppConfig
import com.techniquesmyanmar.greennotes.entity.Notes

class NoteRepository (application: Application) {
    private val db = (application as AppConfig).database

    fun getAllNoteList(id: Int): LiveData<List<Notes>> {
        return db.noteDao.getAllNotes(id)
    }

    fun getSpecificNote(id: Int): LiveData<Notes> {
        return db.noteDao.getSpecificNote(id)
    }

    suspend fun insertNotes(notes: Notes){
        db.noteDao.insertNotes(notes)
    }

    fun searchNoteListing(query: String): LiveData<List<Notes>> {
        return db.noteDao.searchNoteListing(query)
    }

    suspend fun deleteNote(notes: Notes){
        db.noteDao.deleteNote(notes)
    }

    suspend fun deleteNoteById(id:Int){
        return db.noteDao.deleteSpecificNote(id)
    }

    suspend fun deleteUserData(id:Int){
        return db.noteDao.deleteUserData(id)
    }

}