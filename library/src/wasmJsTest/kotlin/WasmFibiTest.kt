package com.jesusdmedinac.jsontocompose

import kotlin.test.Test
import kotlin.test.assertEquals

class WasmFibiTest {

    @Test
    fun `test3rdElement`() {
        assertEquals(11, generateFibi().take(3).last())
    }
}