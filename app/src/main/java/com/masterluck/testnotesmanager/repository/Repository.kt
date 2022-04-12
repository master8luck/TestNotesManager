package com.masterluck.testnotesmanager.repository

import androidx.lifecycle.LiveData
import com.masterluck.testnotesmanager.database.NotesDao
import com.masterluck.testnotesmanager.database.NotesDatabase
import com.masterluck.testnotesmanager.model.Note
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val database: NotesDatabase) {

    private val dao: NotesDao
        get() = database.notesDao

    fun getNotes(): LiveData<List<Note>> = dao.getAll()

    fun getNoteById(id: Long) = dao.getById(id)

    fun insert(note: Note) {
        dao.insert(note)
    }

    private fun generateNotes() {
        mutableListOf(
            Note(1, "qwe", "asd", 123),
            Note(2, "rthw", "trhhrthw4b65uygv35", 432),
            Note(3, "vwbrt", "hv35hb3h5  h35b3 ", 6572363),
        ).forEach {
            it.time = Calendar.getInstance().time.time
            dao.insert(it)
        }
    }

    fun update(note: Note) {
        dao.update(note)
    }

    fun delete(note: Note) {
        dao.delete(note)
    }


}