package com.jesusdmedinac.compose.sdui.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.jesusdmedinac.compose.sdui.presentation.ui.initializer.LocalSupabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.signInAnonymously

object AuthScreen : Screen {
    @Composable
    override fun Content() {
        val auth = LocalSupabase.currentOrThrow.auth
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            auth.signInAnonymously()

            if (auth.currentUserOrNull() == null) {
                navigator.push(LoginScreen)
                return@LaunchedEffect
            }
            navigator.push(MainScreen)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                CircularProgressIndicator()
                Text("Loading...")
            }
        }
    }
}