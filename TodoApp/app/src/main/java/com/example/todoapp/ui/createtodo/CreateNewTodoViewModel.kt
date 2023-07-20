package com.example.todoapp.ui.createtodo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoDao
import com.example.todoapp.data.TodoItem
import kotlinx.coroutines.launch

class CreateNewTodoViewModel(private val todoDao: TodoDao) : ViewModel() {
    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    fun updateTitle(input: String) {
        title = input
    }

    fun updateDescription(input: String) {
        description = input
    }

    fun createTodoItem() {
        viewModelScope.launch {
            val newToDoItem = TodoItem(title = title, description = description)
            todoDao.insert(newToDoItem)
        }
    }
}
