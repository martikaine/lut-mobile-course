package com.example.todoapp.data

import android.content.Context

interface AppContainer {
    val todoDao: TodoDao
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val todoDao: TodoDao by lazy {
        TodoDatabase.getDatabase(context).todoDao()
    }
}
