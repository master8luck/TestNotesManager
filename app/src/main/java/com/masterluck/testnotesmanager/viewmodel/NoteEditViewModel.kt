package com.masterluck.testnotesmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.masterluck.testnotesmanager.model.Note
import com.masterluck.testnotesmanager.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var id by Delegates.notNull<Long>()
    lateinit var note: Note

    fun init() {
        note = repository.getNoteById(id)!!
    }

    fun update(title: String, description: String) {
        if (note.title != title || note.description != description) {
            note.title = title
            note.description = description
            note.time = Calendar.getInstance().timeInMillis
            repository.update(note)
        }
    }

}