package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

internal data class PagerLogicState(
    val pagerState: PagerState,
    val userScrollEnabled: Boolean,
    val beyondViewportPageCount: Int,
    val pages: List<ComposeNode>
)

@Composable
internal fun rememberPagerLogic(
    props: NodeProperties.PagerProps
): PagerLogicState {
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

    // Safety checks for initial page
    val safeInitialPage = if (pageCount > 0) {
        currentPage.coerceIn(0, pageCount - 1)
    } else {
        0
    }

    val pagerState = rememberPagerState(
        initialPage = safeInitialPage,
        pageCount = { pageCount }
    )

    // Sync from state host to pager state (programmatic scroll)
    LaunchedEffect(currentPage) {
        val targetPage = if (pageCount > 0) {
            currentPage.coerceIn(0, pageCount - 1)
        } else {
            0
        }
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    // Sync from pager state back to state host (user swipe)
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentPage) {
            currentPageStateHost?.onStateChange(pagerState.currentPage)
        }
    }

    // Safety check for beyondViewportPageCount
    val safeBeyondViewportPageCount = maxOf(0, props.beyondViewportPageCount ?: 0)

    return PagerLogicState(
        pagerState = pagerState,
        userScrollEnabled = userScrollEnabled,
        beyondViewportPageCount = safeBeyondViewportPageCount,
        pages = pages
    )
}
