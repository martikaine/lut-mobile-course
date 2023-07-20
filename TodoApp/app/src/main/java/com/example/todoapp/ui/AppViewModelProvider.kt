package com.example.todoapp.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todoapp.TodoApplication
import com.example.todoapp.ui.createtodo.CreateNewTodoViewModel
import com.example.todoapp.ui.home.HomeScreenViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel(todoApplication().container.todoDao)
        }

        initializer {
            CreateNewTodoViewModel(todoApplication().container.todoDao)
        }
    }
}

// Retrieve the TodoApplication class which holds the DI container. Its name must be defined in AndroidManifest.xml
fun CreationExtras.todoApplication(): TodoApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)
