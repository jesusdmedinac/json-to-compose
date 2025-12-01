package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jesusdmedinac.jsontocompose.model.ComposeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ComposeComponentsScreenModel : ScreenModel, ComposeComponentsBehavior {
    private val _state = MutableStateFlow(ComposeComponentsState())
    val state = _state.asStateFlow()

    private val _sideEffect =
        MutableStateFlow<ComposeComponentsSideEffect>(ComposeComponentsSideEffect.Idle)
    val sideEffect = _sideEffect.asStateFlow()

    override fun onKeywordChanged(keyword: String) {
        _state.update { state ->
            state.copy(
                keyword = keyword
            )
        }
    }

    override fun onComposeTypeClick(type: ComposeType) {
        screenModelScope.launch {
            _sideEffect.emit(ComposeComponentsSideEffect.ComposeTypeClicked(type))
            delay(100)
            _sideEffect.emit(ComposeComponentsSideEffect.Idle)
        }
    }
}

data class ComposeComponentsState(
    val keyword: String = "",
)

sealed class ComposeComponentsSideEffect {
    data object Idle : ComposeComponentsSideEffect()
    data class ComposeTypeClicked(val type: ComposeType) : ComposeComponentsSideEffect()
}

interface ComposeComponentsBehavior {
    fun onKeywordChanged(keyword: String)
    fun onComposeTypeClick(type: ComposeType)

    companion object {
        val Default = object : ComposeComponentsBehavior {
            override fun onKeywordChanged(keyword: String) {
                TODO("onKeywordChanged is not implemented")
            }

            override fun onComposeTypeClick(type: ComposeType) {
                TODO("onComposeTypeClick is not implemented")
            }
        }
    }
}