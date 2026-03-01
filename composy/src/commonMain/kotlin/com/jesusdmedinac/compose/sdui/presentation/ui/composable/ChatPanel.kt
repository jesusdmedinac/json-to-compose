package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatPanel(modifier: Modifier = Modifier) {
    var message by remember { mutableStateOf("") }
    
    Surface(
        modifier = modifier,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Chat",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item {
                    Text("AI Assistant: How can I help you refine your project?")
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Message input
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text("Type your message...") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(8.dp))
            
            Button(
                onClick = { /* TODO: Send message */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send")
            }
        }
    }
}
