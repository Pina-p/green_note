package com.techniquesmyanmar.greennotes

import android.app.Application
import com.techniquesmyanmar.greennotes.database.NoteDatabase

class AppConfig : Application() {

    lateinit var database: NoteDatabase
    override fun onCreate() {
        super.onCreate()
        database = NoteDatabase.getDatabase(applicationContext)
    }
}