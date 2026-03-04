package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DictionaryScreen()
            }
        }
    }
}

@Composable
fun DictionaryScreen() {
    val context = LocalContext.current

    var searchInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var newWordMK by remember { mutableStateOf("") }
    var newWordEN by remember { mutableStateOf("") }

    // Load dictionary from internal storage if exists, else from raw resource
    var dictionary by remember { mutableStateOf(loadDictionary(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        OutlinedTextField(
            value = searchInput,
            onValueChange = { searchInput = it },
            label = { Text("Внеси збор (МК или EN)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                result = searchWord(searchInput, dictionary)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Пребарај")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = result,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Додавање нов збор ---
        Text("Додади нов збор", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = newWordMK,
            onValueChange = { newWordMK = it },
            label = { Text("Македонски") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newWordEN,
            onValueChange = { newWordEN = it },
            label = { Text("Англиски") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (newWordMK.isNotBlank() && newWordEN.isNotBlank()) {
                    saveWord(context, newWordMK, newWordEN)
                    dictionary[newWordMK.lowercase()] = newWordEN.lowercase()
                    newWordMK = ""
                    newWordEN = ""
                    result = "Зборот е зачуван."
                } else {
                    result = "Внеси и двата зборa!"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зачувај збор")
        }
    }
}

fun loadDictionary(context: Context): MutableMap<String, String> {
    val dictionary = mutableMapOf<String, String>()
    val file = File(context.filesDir, "dictionary.txt")

    if (file.exists()) {
        file.forEachLine(Charsets.UTF_8) { line ->
            val parts = line.split("=")
            if (parts.size == 2) {
                dictionary[parts[0].trim().lowercase()] = parts[1].trim().lowercase()
            }
        }
    } else {
        // Прво вчитување од raw resource
        val inputStream = context.resources.openRawResource(R.raw.dictionary)
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
        reader.forEachLine { line ->
            val parts = line.split("=")
            if (parts.size == 2) {
                dictionary[parts[0].trim().lowercase()] =
                    parts[1].trim().lowercase()
            }
        }
        reader.close()
        // Запиши го во internal storage за понатамошна употреба
        saveDictionaryToFile(context, dictionary)
    }
    return dictionary
}


fun searchWord(word: String, dictionary: Map<String, String>): String {
    val lowerWord = word.lowercase()
    if (dictionary.containsKey(lowerWord)) {
        return "Превод: ${dictionary[lowerWord]}"
    }
    val found = dictionary.entries.find { it.value == lowerWord }
    if (found != null) {
        return "Превод: ${found.key}"
    }
    return "Зборот не постои во речникот."
}

fun saveWord(context: Context, key: String, value: String) {
    val file = File(context.filesDir, "dictionary.txt")
    file.appendText("\n${key.lowercase()}=${value.lowercase()}", Charsets.UTF_8)
}
fun saveDictionaryToFile(context: Context, dictionary: Map<String, String>) {
    val file = File(context.filesDir, "dictionary.txt")
    file.printWriter(Charsets.UTF_8).use { out ->
        dictionary.forEach { (k, v) ->
            out.println("$k=$v")
        }
    }
}