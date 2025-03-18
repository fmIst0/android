package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FontSelectorApp()
        }
    }
}

@Composable
fun FontSelectorApp() {
    var text by remember { mutableStateOf("") }
    var selectedFont by remember { mutableStateOf<FontFamily>(FontFamily.SansSerif) }
    var displayText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Помилка") },
            text = { Text("Будь ласка, введіть текст перед натисканням 'ОК'") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("ОК")
                }
            }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Оберіть шрифт:")
        Row {
            RadioButtonOption("Sans-serif", FontFamily.SansSerif, selectedFont) {
                selectedFont = it
            }
            RadioButtonOption("Serif", FontFamily.Serif, selectedFont) { selectedFont = it }
            RadioButtonOption("Monospace", FontFamily.Monospace, selectedFont) { selectedFont = it }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                if (text.isBlank()) {
                    showDialog = true
                } else {
                    displayText = text
                }
            }) {
                Text("ОК")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                text = ""
                displayText = ""
            }) {
                Text("Cancel")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        BasicText(displayText, style = TextStyle(fontFamily = selectedFont, fontSize = 20.sp))
    }
}

@Composable
fun RadioButtonOption(
    label: String,
    font: FontFamily,
    selectedFont: FontFamily,
    onSelect: (FontFamily) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        RadioButton(
            selected = (font == selectedFont),
            onClick = { onSelect(font) }
        )
        Text(text = label, fontFamily = font, modifier = Modifier.padding(start = 4.dp))
    }
}
