@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.exercise1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exercise1.ui.theme.Exercise1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Exercise1Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Calculator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Calculator() {
    var firstNumber by remember { mutableStateOf("") }
    var secondNumber by remember { mutableStateOf("") }
    var addResult by remember { mutableStateOf("Result") }

    val addNumbers = { a: String, b: String -> (a.toInt() + b.toInt()).toString() }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NumberTextField(
            value = firstNumber,
            onValueChange = { firstNumber = it },
            labelText = "First number"
        )
        NumberTextField(
            value = secondNumber,
            onValueChange = { secondNumber = it },
            labelText = "Second number"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            addResult = addNumbers(firstNumber, secondNumber)
        }) {
            Text("Add")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = addResult,
            fontSize = 48.sp
        )
    }
}

@Composable
fun NumberTextField(value: String, onValueChange: (String) -> Unit, labelText: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text(labelText) },
        placeholder = { Text("Enter a number") }
    )
}

