package com.websarva.wings.dostudy_android.model.Room.ToDoData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoDataTable (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
)