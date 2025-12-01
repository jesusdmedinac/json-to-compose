package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            state.copy(deviceOrientation = DeviceOrientation.Portrait)
        }
    }

    override fun onLandscapeClick() {
        _state.update { state ->
            state.copy(deviceOrientation = DeviceOrientation.Landscape)
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
    val deviceOrientation: DeviceOrientation = DeviceOrientation.Portrait,
) {
    val deviceSize: DeviceSize = when (deviceType to deviceOrientation) {
        DeviceType.Smartphone to DeviceOrientation.Portrait -> DeviceSize(412, 917)
        DeviceType.Smartphone to DeviceOrientation.Landscape -> DeviceSize(917, 412)
        DeviceType.Tablet to DeviceOrientation.Portrait -> DeviceSize(800, 1280)
        DeviceType.Tablet to DeviceOrientation.Landscape -> DeviceSize(1280, 800)
        DeviceType.Desktop to DeviceOrientation.Portrait -> DeviceSize(1024, 1440)
        DeviceType.Desktop to DeviceOrientation.Landscape -> DeviceSize(1440, 1024)

        else -> DeviceSize()
    }
}

data class DeviceSize(
    val width: Int = 0,
    val height: Int = 0,
)

enum class DeviceOrientation {
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