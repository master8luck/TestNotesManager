package com.masterluck.testnotesmanager.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.masterluck.testnotesmanager.database.NotesDao
import com.masterluck.testnotesmanager.database.NotesDatabase
import com.masterluck.testnotesmanager.model.Note
import com.masterluck.testnotesmanager.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val database: NotesDatabase,
    @ApplicationContext private val context: Context,
) {

    private val dao: NotesDao
        get() = database.notesDao

    fun getNotes(): LiveData<List<Note>> = dao.getAll()

    fun getNoteById(id: Long) = dao.getById(id)

    fun insert(note: Note) {
        dao.insert(note)
    }

    fun generateNotes(): Single<Boolean> {

        return Single.fromCallable {
            Thread.sleep(5000)
            if (!Utils.isNetworkAvailable(context)) {
                false
            }
            else {
                mutableListOf(
                    Note(1, "Read 100 books", "optional - in 1 year", 98912624563),
                    Note(2, "make something\n\n\nor not..", "", 299945433332),
                    Note(
                        3,
                        "go walk",
                        "very long desc\nwith\nsome\ndummy\ndata\n\nhere",
                        9996572363
                    ),
                ).forEach {
                    dao.insert(it)
                }
                true
            }
        }
    }

    fun update(note: Note) {
        dao.update(note)
    }

    fun delete(note: Note) {
        dao.delete(note)
    }


}