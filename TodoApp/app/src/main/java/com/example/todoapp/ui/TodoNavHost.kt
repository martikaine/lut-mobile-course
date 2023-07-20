package com.example.todoapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.createtodo.CreateNewTodoScreen
import com.example.todoapp.ui.home.HomeScreen

@Composable
fun TodoNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            HomeScreen(onAddNewClick = { navController.navigate("create_todo") })
        }
        composable("create_todo") {
            CreateNewTodoScreen(onExit = { navController.navigate("main") })
        }
    }
}