package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.Composable
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

@Composable
fun ComposeNode.ToCustom() {
    val customRenderers = LocalCustomRenderers.current
    val customProps = properties as? NodeProperties.CustomProps
    if (customProps == null) {
        println("Warning: NodeProperties is not CustomProps for node of type Custom.")
        return
    }
    val customType = customProps.customType
    if (customType != null) {
        val renderer = customRenderers[customType]
        if (renderer != null) {
            renderer.invoke(this)
        } else {
            println("Warning: Custom renderer for type \"$customType\" not found in LocalCustomRenderers.")
        }
    } else {
        println("Warning: customType is null in CustomProps.")
    }
}
