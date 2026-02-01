package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.runtime.Composable
import com.jesusdmedinac.jsontocompose.LocalCustomRenderers
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

@Composable
fun ComposeNode.ToCustom() {
    val customRenderers = LocalCustomRenderers.current
    val customProps = properties as? NodeProperties.CustomProps
    customProps?.customType?.let { customType ->
        customRenderers[customType]?.invoke(this)
    }
}
