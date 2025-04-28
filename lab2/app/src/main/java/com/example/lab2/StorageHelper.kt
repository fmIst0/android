package com.example.lab2

import android.content.Context

object StorageHelper {
    private const val FILE_NAME = "storage.txt"

    fun saveData(context: Context, data: String): Boolean {
        return try {
            context.openFileOutput(FILE_NAME, Context.MODE_APPEND).use { output ->
                output.write((data + "\n").toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun readData(context: Context): List<String> {
        return try {
            context.openFileInput(FILE_NAME).bufferedReader().readLines()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
