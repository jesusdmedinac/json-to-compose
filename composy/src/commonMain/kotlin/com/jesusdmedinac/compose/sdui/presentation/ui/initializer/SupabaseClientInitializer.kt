package com.jesusdmedinac.compose.sdui.presentation.ui.initializer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

val LocalSupabase: ProvidableCompositionLocal<SupabaseClient?> =
    staticCompositionLocalOf { null }

@Composable
fun SupabaseClientInitializer(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSupabase provides createSupabaseClient(
        supabaseUrl = "https://qywslggybvfcbnxbulqx.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF5d3NsZ2d5YnZmY2JueGJ1bHF4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzU0MTE3NjMsImV4cCI6MjA1MDk4Nzc2M30.md7ZWwhQQTKSorVMpzqwS10Nlqkaw-KBJGpvbGIb_wA"
    ) {
        install(Auth) {}
    }) {
        content()
    }
}