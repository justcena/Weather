package com.example.weather.utils

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class Watcher : TextWatcher {
    @ExperimentalCoroutinesApi
    private val channel = ConflatedBroadcastChannel<String>()
    override fun afterTextChanged(editable: Editable?) { channel.offer(editable.toString())}
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
    fun asFlow(): Flow<String> {
        return channel.asFlow()
    }
}