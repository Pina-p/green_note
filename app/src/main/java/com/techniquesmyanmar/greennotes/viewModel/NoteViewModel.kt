package com.techniquesmyanmar.greennotes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techniquesmyanmar.greennotes.entity.Notes
import com.techniquesmyanmar.greennotes.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel (application: Application) : AndroidViewModel(application) {
    private val repository : NoteRepository by lazy {NoteRepository(application)}

    var noteSearch = MutableLiveData<String>()

    fun getAllNotes(id:Int) : LiveData<List<Notes>> {
        return repository.getAllNoteList(id)
    }

    fun getSpecificNote(id:Int) : LiveData<Notes> {
        return repository.getSpecificNote(id)
    }
    fun insertNotes(notes: Notes){
        viewModelScope.launch {
            repository.insertNotes(notes)
        }
    }

    fun deleteNote(notes: Notes){
        viewModelScope.launch {
            repository.deleteNote(notes)
        }
    }

    fun deleteNoteById (id : Int){
        viewModelScope.launch {
            repository.deleteNoteById(id)
        }
    }

    fun deleteUserData (id: Int){
        viewModelScope.launch {
            repository.deleteUserData(id)
        }
    }
    fun searchNoteList(query: String):LiveData<List<Notes>>{
        return repository.searchNoteListing(query)
    }


}