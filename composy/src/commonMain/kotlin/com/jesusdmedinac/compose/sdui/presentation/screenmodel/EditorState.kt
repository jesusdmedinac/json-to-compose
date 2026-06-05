package com.jesusdmedinac.compose.sdui.presentation.screenmodel


import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties

data class EditorState(
    val rootNode: ComposeNode = ComposeNode(
        type = ComposeType.Column,
        properties = NodeProperties.ColumnProps()
    ),
    val selectedNodeId: String? = null,
    val isLeftPanelDisplayed: Boolean = true,
    val isRightPanelDisplayed: Boolean = true,
    val deviceType: DeviceType = DeviceType.Smartphone,
    val deviceOrientation: DeviceOrientation = DeviceOrientation.Portrait,
    val searchKeyword: String = "",
    val availableComposeTypes: List<ComposeType> = ComposeType.entries
)
