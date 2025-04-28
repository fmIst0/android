package com.example.lab2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)

        val textView = findViewById<TextView>(R.id.textViewData)

        val data = StorageHelper.readData(this)

        if (data.isEmpty()) {
            textView.text = "Дані відсутні."
        } else {
            textView.text = data.joinToString("\n")
        }
    }
}
