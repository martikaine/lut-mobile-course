package com.example.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_items ORDER BY isCompleted")
    fun getAll(): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDoItem: TodoItem): Long

    @Update
    suspend fun update(toDoItem: TodoItem)

    @Delete
    suspend fun delete(toDoItem: TodoItem)
}