package com.example.todoapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.data.TodoItem
import com.example.todoapp.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddNewClick: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val toDoItems by viewModel.toDoItems.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("ToDo List")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewClick) {
                Icon(Icons.Default.Add, contentDescription = "Create todo item")
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                ToDoList(toDoItems, viewModel::markAsCompleted)
                if (viewModel.isDeleteDialogOpen) DeleteAlertDialog(
                    itemToDelete = viewModel.itemToBeDeleted!!,
                    onDelete = { viewModel.confirmDeletion() },
                    onDismissRequest = { viewModel.cancelDelete() }
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoList(
    toDoItems: List<TodoItem>,
    onItemComplete: (TodoItem) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(toDoItems, key = { it.id }) { toDoItem ->
            Box(Modifier.animateItemPlacement()) {
                TodoListItem(toDoItem, onItemComplete)
            }
        }
    }
}

@Composable
fun TodoListItem(
    toDoItem: TodoItem,
    onItemComplete: (TodoItem) -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val alpha by animateFloatAsState(if (toDoItem.isCompleted) 0.5f else 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.toggleExpand(toDoItem.id) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = alpha
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = toDoItem.isCompleted,
                    onCheckedChange = { onItemComplete(toDoItem) }
                )
                Text(
                    text = toDoItem.title,
                    modifier = Modifier.alpha(alpha),
                    style = MaterialTheme.typography.titleSmall
                )
                IconButton(onClick = { viewModel.beginDelete(toDoItem) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            AnimatedVisibility(visible = viewModel.isExpanded(toDoItem.id)) {
                Text(
                    text = toDoItem.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .alpha(alpha)
                )
            }
        }
    }
}

@Composable
fun DeleteAlertDialog(
    itemToDelete: TodoItem,
    onDelete: (TodoItem) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Are you sure?") },
        text = {
            Text("You are about to delete \"${itemToDelete.title}\". This action is irreversible.")
        },
        confirmButton = {
            TextButton(
                onClick = { onDelete(itemToDelete) }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        },
    )
}
