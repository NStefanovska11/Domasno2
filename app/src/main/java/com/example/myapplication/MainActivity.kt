package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SearchScreen()
            }
        }
    }
}

@Composable
fun SearchScreen() {

    var tagInput by remember { mutableStateOf("") }
    var tagList by remember { mutableStateOf(listOf("Android", "Kotlin", "Compose")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = tagInput,
            onValueChange = { tagInput = it },
            label = { Text("Tag name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (tagInput.isNotBlank()) {
                    tagList = tagList + tagInput
                    tagInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tagList) { tag ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { tagList = emptyList() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear All")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    MaterialTheme {
        SearchScreen()
    }
}