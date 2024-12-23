package com.jesusdmedinac.compose.sdui.di

import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.EditNodeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import org.koin.dsl.module

fun appModule() = module {
    single { MainScreenModel() }
    single { ComposeTreeScreenModel() }
    single { EditNodeScreenModel() }
}
