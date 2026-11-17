package com.jesusdmedinac.compose.sdui.presentation.screenmodel


import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties

data class EditorState(
    val rootNode: ComposeNode = ComposeNode(
        type = ComposeType.Column,
        properties = NodeProperties.ColumnProps(),
        id = "root"
    ),
    val selectedNodeId: String? = null,
    val isLeftPanelDisplayed: Boolean = true,
    val isRightPanelDisplayed: Boolean = true,
    val deviceType: DeviceType = DeviceType.Smartphone,
    val deviceOrientation: DeviceOrientation = DeviceOrientation.Portrait,
    val searchKeyword: String = ""
) {
    val deviceSize: DeviceSize
        get() = when (deviceType to deviceOrientation) {
            DeviceType.Smartphone to DeviceOrientation.Portrait -> DeviceSize(412, 917)
            DeviceType.Smartphone to DeviceOrientation.Landscape -> DeviceSize(917, 412)
            DeviceType.Tablet to DeviceOrientation.Portrait -> DeviceSize(800, 1280)
            DeviceType.Tablet to DeviceOrientation.Landscape -> DeviceSize(1280, 800)
            DeviceType.Desktop to DeviceOrientation.Portrait -> DeviceSize(1024, 1440)
            DeviceType.Desktop to DeviceOrientation.Landscape -> DeviceSize(1440, 1024)
            else -> DeviceSize()
        }

    val filteredComposeTypes: List<ComposeType>
        get() = if (searchKeyword.isBlank()) {
            ComposeType.entries
        } else {
            ComposeType.entries.filter { it.name.contains(searchKeyword, ignoreCase = true) }
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
