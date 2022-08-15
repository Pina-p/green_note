package com.techniquesmyanmar.greennotes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.techniquesmyanmar.greennotes.entity.Notes
import com.techniquesmyanmar.greennotes.entity.User
import com.techniquesmyanmar.greennotes.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel  (application: Application) : AndroidViewModel(application) {

    private val repository : UserRepository by lazy { UserRepository(application) }

    fun insertUser(user: User){
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun getAllUsers(query:String) : LiveData<List<User>> {
        return repository.getAllUsers(query)
    }

    fun deleteUserById (id : Int){
        viewModelScope.launch {
            repository.deleteUserById(id)
        }
    }

    fun getUserById(id:Int) : LiveData<User> {
        return repository.getUserById(id)
    }
}