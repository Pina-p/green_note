package com.techniquesmyanmar.greennotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techniquesmyanmar.greennotes.dao.NoteDao
import com.techniquesmyanmar.greennotes.dao.UserDao
import com.techniquesmyanmar.greennotes.entity.Notes
import com.techniquesmyanmar.greennotes.entity.User

@Database(entities = [Notes::class,User::class], version = 4, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao : NoteDao
    abstract val userDao : UserDao
    companion object{
        var noteDatabase: NoteDatabase ?= null

        fun getDatabase(context: Context):NoteDatabase{
            if(noteDatabase == null){
                noteDatabase = Room.databaseBuilder(context,NoteDatabase::class.java,"note_database",)
                    .fallbackToDestructiveMigration().build()
            }
            return noteDatabase!!
        }
    }
}