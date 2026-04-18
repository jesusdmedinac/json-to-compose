package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from

@Composable
fun ComposeNode.ToVerticalPager() {
    val props = properties as? NodeProperties.PagerProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (pagerState, userScrollEnabled, beyondViewportPageCount, pages) = rememberPagerLogic(props)

    VerticalPager(
        state = pagerState,
        modifier = modifier,
        beyondViewportPageCount = beyondViewportPageCount,
        userScrollEnabled = userScrollEnabled
    ) { page ->
        pages.getOrNull(page)?.ToCompose()
    }
}
