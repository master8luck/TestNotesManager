package com.masterluck.testnotesmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var title: String,
    var description: String,
    var time: Long,
)