package com.jesusdmedinac.jsontocompose

import kotlin.test.Test
import kotlin.test.assertEquals

class FibiTest {

    @Test
    fun `test3rdElement`() {
        assertEquals(firstElement + secondElement, generateFibi().take(3).last())
    }
}