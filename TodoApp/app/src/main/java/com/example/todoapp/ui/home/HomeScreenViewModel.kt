package com.example.todoapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoDao
import com.example.todoapp.data.TodoItem
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val todoDao: TodoDao) : ViewModel() {
    val toDoItems = todoDao.getAll()

    private var expandedItems = mutableStateListOf<Int>()

    var itemToBeDeleted: TodoItem? = null
        private set

    var isDeleteDialogOpen by mutableStateOf(false)
        private set

    fun toggleExpand(id: Int) {
        if (expandedItems.contains(id)) {
            expandedItems.remove(id)
        } else {
            expandedItems.add(id)
        }
    }

    fun isExpanded(id: Int): Boolean {
        return expandedItems.contains(id)
    }

    fun beginDelete(item: TodoItem) {
        itemToBeDeleted = item
        isDeleteDialogOpen = true
    }

    fun cancelDelete() {
        itemToBeDeleted = null
        isDeleteDialogOpen = false
    }

    fun markAsCompleted(todoItem: TodoItem) {
        viewModelScope.launch {
            val updatedItem = todoItem.copy(isCompleted = !todoItem.isCompleted)
            todoDao.update(updatedItem)
        }
    }

    fun confirmDeletion() {
        if (itemToBeDeleted == null) return

        viewModelScope.launch {
            todoDao.delete(itemToBeDeleted!!)
            itemToBeDeleted = null
            isDeleteDialogOpen = false
        }
    }
}
