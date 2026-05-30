package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNode.ToTopAppBar() {
    val props = properties as? NodeProperties.TopAppBarProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val backgroundColor = props.backgroundColor.toColor(Color.Unspecified)
    val contentColor = props.contentColor.toColor(Color.Unspecified)

    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = backgroundColor,
        titleContentColor = contentColor,
        navigationIconContentColor = contentColor,
        actionIconContentColor = contentColor
    )

    val titleComposable: @Composable () -> Unit = {
        props.title?.ToCompose()
    }
    val navigationIconComposable: @Composable () -> Unit = {
        props.navigationIcon?.ToCompose()
    }
    val actionsComposable: @Composable RowScope.() -> Unit = {
        props.actions?.forEach { it.ToCompose() }
    }

    when (type) {
        ComposeType.CenterAlignedTopAppBar -> {
            CenterAlignedTopAppBar(
                title = titleComposable,
                navigationIcon = navigationIconComposable,
                actions = actionsComposable,
                colors = colors,
                modifier = modifier
            )
        }
        ComposeType.MediumTopAppBar -> {
            MediumTopAppBar(
                title = titleComposable,
                navigationIcon = navigationIconComposable,
                actions = actionsComposable,
                colors = colors,
                modifier = modifier
            )
        }
        ComposeType.LargeTopAppBar -> {
            LargeTopAppBar(
                title = titleComposable,
                navigationIcon = navigationIconComposable,
                actions = actionsComposable,
                colors = colors,
                modifier = modifier
            )
        }
        else -> {
            TopAppBar(
                title = titleComposable,
                navigationIcon = navigationIconComposable,
                actions = actionsComposable,
                colors = colors,
                modifier = modifier
            )
        }
    }
}
