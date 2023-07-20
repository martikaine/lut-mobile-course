@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.exercise2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exercise2.ui.theme.Exercise2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Exercise2Theme {
                AppRoot()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview(showBackground = true)
fun AppRoot() {
    val navController = rememberNavController()
    val textForSecondActivity = "Hello from the main activity"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.background
                )
            )
        },
        content = {
            NavHost(navController = navController, startDestination = "main_buttons") {
                composable("main_buttons") {
                    MainButtons(onSecondActivityClick = {
                        navController.navigate("second_activity/$textForSecondActivity")
                    })
                }
                composable("second_activity/{text}") { backStackEntry ->
                    SecondActivity(text = backStackEntry.arguments?.getString("text"),
                        onGoBackClick = { navController.navigate("main_buttons") })
                }
            }
        }
    )


}

@Composable
fun MainButtons(onSecondActivityClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            GoToSecondActivityButton(onClick = onSecondActivityClick)
            GoToGoogleButton()
        }
    }
}

@Composable
fun GoToSecondActivityButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Second activity")
    }
}

@Composable
fun GoToGoogleButton() {
    val uriHandler = LocalUriHandler.current

    Button(onClick = {
        uriHandler.openUri("https://www.google.com/")
    }) {
        Text("Google")
    }
}

@Composable
fun SecondActivity(text: String?, onGoBackClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text ?: "")
            Button(onClick = onGoBackClick) {
                Text("Go back")
            }
        }
    }
}