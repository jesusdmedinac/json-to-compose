package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProjectGeneratorPanel(
    modifier: Modifier = Modifier
) {
    var projectDescription by remember { mutableStateOf("") }
    
    Column(modifier = modifier) {
        Text(
            "Project Generator",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = projectDescription,
            onValueChange = { projectDescription = it },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            label = { Text("Describe your project in natural language") },
            placeholder = { Text("I want a mobile and desktop app with a dark theme...") }
        )
        
        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = { /* TODO: Generate project */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate With AI")
        }
        
        Spacer(Modifier.height(24.dp))
        
        ConfigurationSection()
    }
}

@Composable
private fun ConfigurationSection() {
    var applicationType by remember { mutableStateOf("Mobile") }
    var targetPlatforms by remember { mutableStateOf(setOf("Android", "iOS")) }
    var projectName by remember { mutableStateOf("") }
    var packageName by remember { mutableStateOf("") }
    
    Column {
        Text(
            "Configuration",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(Modifier.height(16.dp))
        
        // Application Type
        OutlinedTextField(
            value = applicationType,
            onValueChange = { applicationType = it },
            label = { Text("Application Type") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(8.dp))
        
        // Target Platforms
        OutlinedTextField(
            value = targetPlatforms.joinToString(", "),
            onValueChange = { /* TODO: Handle platforms selection */ },
            label = { Text("Target Platforms") },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Project details
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = projectName,
            onValueChange = { projectName = it },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = packageName,
            onValueChange = { packageName = it },
            label = { Text("Package Name") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { /* TODO: Download project */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Project")
        }
    }
}
