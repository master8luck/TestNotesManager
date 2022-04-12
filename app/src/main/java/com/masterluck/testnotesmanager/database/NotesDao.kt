package com.masterluck.testnotesmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.masterluck.testnotesmanager.model.Note


@Dao
interface NotesDao {

    @Query("SELECT * FROM note ORDER by time DESC ")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getById(id: Long): Note?

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

}