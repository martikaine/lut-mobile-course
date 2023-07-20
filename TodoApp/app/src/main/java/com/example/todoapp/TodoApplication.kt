package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.AppContainer
import com.example.todoapp.data.AppDataContainer

class TodoApplication : Application() {
    /** The rest of the application obtains dependencies from this AppContainer. */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}