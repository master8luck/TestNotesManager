package com.masterluck.testnotesmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.masterluck.testnotesmanager.model.Note
import com.masterluck.testnotesmanager.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    val notes: LiveData<List<Note>> by lazy { repository.getNotes() }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    var isDataRefreshed = false

    fun add() {
        repository.insert(
            Note(
                0, "Новая заметка", "",
                Calendar.getInstance().timeInMillis
            )
        )
    }

    fun delete(note: Note) {
        repository.delete(note)
    }


    fun fetchFromWeb(): Single<Boolean> {
        return repository.generateNotes()
    }


}