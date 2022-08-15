package com.techniquesmyanmar.greennotes.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.techniquesmyanmar.greennotes.AppConfig
import com.techniquesmyanmar.greennotes.entity.User

class UserRepository (application: Application) {
    private val db = (application as AppConfig).database

    fun getAllUsers(query: String): LiveData<List<User>> {
        return db.userDao.getAllUsers()
    }

    suspend fun insertUser(user: User){
        db.userDao.insertUser(user)
    }

    suspend fun deleteUserById (id:Int){
        return db.userDao.deleteSpecificUser(id)
    }

    fun getUserById(id: Int): LiveData<User> {
        return db.userDao.getUserById(id)
    }

}