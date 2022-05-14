package com.itis.my.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.my.InfoRepository
import com.itis.my.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val notesLiveData = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>>
        get() = notesLiveData

    fun listenNotesFromDb() {
        viewModelScope.launch {
            InfoRepository.listenNotes().collect { notes ->
                notesLiveData.postValue(notes)
            }
        }


    }

    fun saveNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
                InfoRepository.saveNotes(listOf(note))
        }

    }

}