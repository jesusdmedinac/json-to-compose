package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToPlainTooltip() {
    val props = properties as? NodeProperties.PlainTooltipProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val text = props.text ?: ""
    val tooltipState = rememberTooltipState()

    Box(modifier = modifier) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(text = text)
                }
            },
            state = tooltipState
        ) {
            Box(modifier = Modifier) {
                props.anchor?.ToCompose()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToRichTooltip() {
    val props = properties as? NodeProperties.RichTooltipProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)
    val tooltipState = rememberTooltipState()

    Box(modifier = modifier) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                RichTooltip(
                    title = props.title?.let { { it.ToCompose() } },
                    action = props.action?.let { { it.ToCompose() } }
                ) {
                    props.text?.ToCompose()
                }
            },
            state = tooltipState
        ) {
            Box(modifier = Modifier) {
                props.anchor?.ToCompose()
            }
        }
    }
}

