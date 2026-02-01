package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jesusdmedinac.jsontocompose.LocalDrawableResources
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import org.jetbrains.compose.resources.painterResource

@Composable
fun ComposeNode.ToImage() {
    val props = properties as? NodeProperties.ImageProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val drawableResources = LocalDrawableResources.current

    when {
        props.url != null -> {
            AsyncImage(
                model = props.url,
                contentDescription = props.contentDescription,
                modifier = modifier
            )
        }

        props.resourceName != null -> {
            val resource = drawableResources[props.resourceName]

            if (resource != null) {
                Image(
                    painter = painterResource(resource),
                    contentDescription = props.contentDescription,
                    modifier = modifier
                )
            } else {
                Box(modifier = modifier.background(Color.LightGray)) {
                    Text("Res not found: ${props.resourceName}", modifier = Modifier.padding(4.dp))
                }
            }
        }

        else -> {
            Box(modifier = modifier.background(Color.Magenta))
        }
    }
}
