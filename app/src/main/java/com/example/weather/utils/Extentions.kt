package com.example.weather.utils

import android.widget.EditText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

private const val TAG = "Extentions"
suspend fun EditText.afterTextChanged(afterTextChanged: suspend (String) -> Unit) {
    val watcher = Watcher()
    this.addTextChangedListener(watcher)

    watcher.asFlow()
        .debounce(500)
        .collect { afterTextChanged(it) }
}