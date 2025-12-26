package com.example.qm_app.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.qm_app.common.UiEvent
import kotlinx.coroutines.channels.Channel

@Composable
fun Overlay() {
    val uiEventQueue = remember { Channel<UiEvent>(Channel.UNLIMITED) }
    val displayQueue = remember { mutableStateListOf<String>() }

    LaunchedEffect(uiEventQueue) {
        for (event in uiEventQueue) {
            displayQueue.add("")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

    }
}