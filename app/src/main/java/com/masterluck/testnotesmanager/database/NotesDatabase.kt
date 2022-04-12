package com.masterluck.testnotesmanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.masterluck.testnotesmanager.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    abstract val notesDao: NotesDao

}