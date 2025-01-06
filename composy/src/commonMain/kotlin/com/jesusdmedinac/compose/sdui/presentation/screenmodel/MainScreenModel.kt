package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jesusdmedinac.jsontocompose.ComposeNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainScreenModel : ScreenModel, MainScreenBehavior {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    private val _sideEffect = MutableStateFlow<MainScreenSideEffect>(MainScreenSideEffect.Idle)
    val sideEffect: StateFlow<MainScreenSideEffect> = _sideEffect

    override fun onDisplayLeftPanelChange(isLeftPanelDisplayed: Boolean) {
        _state.update { state ->
            state.copy(isLeftPanelDisplayed = isLeftPanelDisplayed)
        }
    }

    override fun onDisplayRightPanelChange(isRightPanelDisplayed: Boolean) {
        _state.update { state ->
            state.copy(isRightPanelDisplayed = isRightPanelDisplayed)
        }
    }

    override fun exportAsJSONClick(composeTree: ComposeNode) {
        screenModelScope.launch {
            _sideEffect.emit(MainScreenSideEffect.ExportAsJSON(
                baseName = "composeAsJSON",
                extension = "json",
                initialDirectory = "",
                content = Json.encodeToString(composeTree)
            ))
            delay(100)
            _sideEffect.emit(MainScreenSideEffect.Idle)
        }
    }

    override fun onDownloadDesktopClick() {
        TODO("Not yet implemented")
    }

    override fun onPortraitClick() {
        _state.update { state ->
            state.copy(orientation = Orientation.Portrait)
        }
    }

    override fun onLandscapeClick() {
        _state.update { state ->
            state.copy(orientation = Orientation.Landscape)
        }
    }

    override fun onSmartphoneClick() {
        _state.update { state ->
            state.copy(deviceType = DeviceType.Smartphone)
        }
    }

    override fun onTabletClick() {
        _state.update { state ->
            state.copy(deviceType = DeviceType.Tablet)
        }
    }

    override fun onDesktopClick() {
        _state.update { state ->
            state.copy(deviceType = DeviceType.Desktop)
        }
    }
}

data class MainScreenState(
    val isLeftPanelDisplayed: Boolean = true,
    val isRightPanelDisplayed: Boolean = false,
    val deviceType: DeviceType = DeviceType.Smartphone,
    val orientation: Orientation = Orientation.Portrait,
)

enum class Orientation {
    Portrait,
    Landscape,
}

enum class DeviceType {
    Smartphone,
    Tablet,
    Desktop,
}

sealed class MainScreenSideEffect {
    data object Idle : MainScreenSideEffect()
    data class ExportAsJSON(
        val baseName: String,
        val extension: String,
        val initialDirectory: String,
        val content: String,
    ) : MainScreenSideEffect()
}

interface MainScreenBehavior {
    fun onDisplayLeftPanelChange(isLeftPanelDisplayed: Boolean)
    fun onDisplayRightPanelChange(isRightPanelDisplayed: Boolean)
    fun exportAsJSONClick(composeTree: ComposeNode)
    fun onDownloadDesktopClick()
    fun onPortraitClick()
    fun onLandscapeClick()
    fun onSmartphoneClick()
    fun onTabletClick()
    fun onDesktopClick()

    companion object {
        val Default = object : MainScreenBehavior {
            override fun onDisplayLeftPanelChange(isLeftPanelDisplayed: Boolean) {
                TODO("onDisplayLeftPanelChange is not implemented")
            }

            override fun onDisplayRightPanelChange(isRightPanelDisplayed: Boolean) {
                TODO("onDisplayRightPanelChange is not implemented")
            }

            override fun exportAsJSONClick(composeTree: ComposeNode) {
                TODO("exportAsJSONClick is not implemented")
            }

            override fun onDownloadDesktopClick() {
                TODO("onDownloadDesktopClick is not implemented")
            }

            override fun onPortraitClick() {
                TODO("onPortraitClick is not implemented")
            }

            override fun onLandscapeClick() {
                TODO("onLandscapeClick is not implemented")
            }

            override fun onSmartphoneClick() {
                TODO("onSmartphoneClick is not implemented")
            }

            override fun onTabletClick() {
                TODO("onTabletClick is not implemented")
            }

            override fun onDesktopClick() {
                TODO("onDesktopClick is not implemented")
            }
        }
    }
}