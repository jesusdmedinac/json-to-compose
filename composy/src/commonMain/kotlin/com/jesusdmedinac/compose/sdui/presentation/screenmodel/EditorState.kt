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
    val deviceSize: DeviceSize = when (deviceType) {
        DeviceType.Smartphone -> when (deviceOrientation) {
            DeviceOrientation.Portrait -> DeviceSize(412, 917)
            DeviceOrientation.Landscape -> DeviceSize(917, 412)
        }
        DeviceType.Tablet -> when (deviceOrientation) {
            DeviceOrientation.Portrait -> DeviceSize(800, 1280)
            DeviceOrientation.Landscape -> DeviceSize(1280, 800)
        }
        DeviceType.Desktop -> when (deviceOrientation) {
            DeviceOrientation.Portrait -> DeviceSize(1024, 1440)
            DeviceOrientation.Landscape -> DeviceSize(1440, 1024)
        }
    }

    val filteredComposeTypes: List<ComposeType> = run {
        val query = searchKeyword.trim()
        if (query.isEmpty()) {
            ComposeType.entries
        } else {
            ComposeType.entries.filter { it.name.contains(query, ignoreCase = true) }
        }
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
