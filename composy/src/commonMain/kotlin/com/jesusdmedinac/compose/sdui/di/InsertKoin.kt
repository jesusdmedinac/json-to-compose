package com.jesusdmedinac.compose.sdui.di

import com.jesusdmedinac.compose.sdui.ComposeEditorScreenModel
import com.jesusdmedinac.compose.sdui.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.MainScreenModel
import org.koin.dsl.module

fun appModule() = module {
    single { MainScreenModel() }
    single { ComposeEditorScreenModel() }
    single { EditNodeScreenModel() }
}
