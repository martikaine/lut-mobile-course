package com.example.exercise3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FruitList(onItemClick: (index: Int) -> Unit) {
    val count = stringArrayResource(R.array.items).size

    LazyColumn {
        items(count) { index ->
            FruitListItem(index, onClick = { onItemClick(index) })
        }
    }
}

@Composable
fun FruitListItem(index: Int, onClick: () -> Unit) {
    val itemName = stringArrayResource(R.array.items)[index]
    val price = stringArrayResource(R.array.prices)[index]
    val description = stringArrayResource(R.array.descriptions)[index]

    Row (
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = itemName,
                fontSize = 24.sp
            )
            Text(
                text = description,
            )
        }
        Text(
            text = price,
            fontSize = 24.sp
        )
    }
}

