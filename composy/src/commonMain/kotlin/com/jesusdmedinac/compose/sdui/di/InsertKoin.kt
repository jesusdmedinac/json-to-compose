package com.jesusdmedinac.compose.sdui.di

import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun dataModule() = module {
    single<io.github.jan.supabase.SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = "https://qywslggybvfcbnxbulqx.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF5d3NsZ2d5YnZmY2JueGJ1bHF4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzU0MTE3NjMsImV4cCI6MjA1MDk4Nzc2M30.md7ZWwhQQTKSorVMpzqwS10Nlqkaw-KBJGpvbGIb_wA"
        ) {
            install(Auth) {
            }
            defaultSerializer = KotlinXSerializer(Json)
        }
    }
    single { get<io.github.jan.supabase.SupabaseClient>().auth }
}

fun appModule() = module {
    single { MainScreenModel() }
    single { ComposeTreeScreenModel() }
    single { EditNodeScreenModel() }
    single { AuthScreenModel(get()) }
}
