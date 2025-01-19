package com.jesusdmedinac.compose.sdui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform