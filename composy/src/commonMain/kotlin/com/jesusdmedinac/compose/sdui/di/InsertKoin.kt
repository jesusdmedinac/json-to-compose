package com.jesusdmedinac.compose.sdui.di

import com.jesusdmedinac.compose.sdui.auth.data.AuthRepositoryImpl
import com.jesusdmedinac.compose.sdui.auth.domain.AuthRepository
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeComponentsScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import com.jesusdmedinac.composy.BuildKonfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun dataModule() = module {
    single<io.github.jan.supabase.SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildKonfig.SUPABASE_URL,
            supabaseKey = BuildKonfig.SUPABASE_KEY,
        ) {
            install(Auth) {}
            defaultSerializer = KotlinXSerializer(Json)
        }
    }
    single { get<io.github.jan.supabase.SupabaseClient>().auth }
    single<AuthRepository>{ AuthRepositoryImpl(get()) }
}

fun appModule() = module {
    single { MainScreenModel() }
    single { ComposeComponentsScreenModel() }
    single { ComposeTreeScreenModel() }
    single { EditNodeScreenModel() }
    single { AuthScreenModel(get()) }
}
