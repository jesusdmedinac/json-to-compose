package com.jesusdmedinac.jsontocompose

import kotlin.test.Test
import kotlin.test.assertEquals

class DesktopFibiTest {

    @Test
    fun `test 3rd element`() {
        assertEquals(5, generateFibi().take(3).last())
    }
}