package com.example.todoapp.ui.createtodo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewTodoScreen(
    onExit: () -> Unit,
    viewModel: CreateNewTodoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Column (Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Cancel", modifier = Modifier.clickable { onExit() })
            Spacer(Modifier.width(16.dp))
            Text("Create ToDo", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                viewModel.createTodoItem()
                onExit()
            }) {
                Text("Save")
            }
        }
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("Title")},
                placeholder = { Text("Add title")},
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text("Description")},
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}
