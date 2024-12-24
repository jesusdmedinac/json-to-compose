package com.jesusdmedinac.compose.sdui

enum class Platform {
    Wasm,
    Desktop
}

expect fun getPlatform(): Platform