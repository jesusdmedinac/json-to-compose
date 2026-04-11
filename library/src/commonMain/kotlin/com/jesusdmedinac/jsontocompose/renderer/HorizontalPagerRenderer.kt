package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToHorizontalPager() {
    val props = properties as? NodeProperties.PagerProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val pages = props.pages ?: emptyList()
    val pageCount = pages.size

    val (currentPage, currentPageStateHost) = resolveStateHostValue(
        stateHostName = props.currentPageStateHostName,
        inlineValue = props.currentPage,
        defaultValue = 0
    )

    val (userScrollEnabled, _) = resolveStateHostValue(
        stateHostName = props.userScrollEnabledStateHostName,
        inlineValue = props.userScrollEnabled,
        defaultValue = true
    )

    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { pageCount }
    )

    // Sync from state host to pager state (programmatic scroll)
    LaunchedEffect(currentPage) {
        if (pagerState.currentPage != currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }
    }

    // Sync from pager state back to state host (user swipe)
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentPage) {
            currentPageStateHost?.onStateChange(pagerState.currentPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        beyondViewportPageCount = props.beyondViewportPageCount ?: 0,
        userScrollEnabled = userScrollEnabled
    ) { page ->
        pages.getOrNull(page)?.ToCompose()
    }
}
