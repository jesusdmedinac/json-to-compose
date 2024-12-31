package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ComposeComponentsScreenModel : ScreenModel, ComposeComponentsBehavior {
    private val _state = MutableStateFlow(ComposeComponentsState())
    val state = _state.asStateFlow()

    override fun onKeywordChanged(keyword: String) {
        _state.update { state ->
            state.copy(
                keyword = keyword
            )
        }
    }
}

data class ComposeComponentsState(
    val keyword: String = "",
)

interface ComposeComponentsBehavior {
    fun onKeywordChanged(keyword: String)

    companion object {
        val Default = object : ComposeComponentsBehavior {
            override fun onKeywordChanged(keyword: String) {
                TODO("onKeywordChanged is not implemented")
            }
        }
    }
}