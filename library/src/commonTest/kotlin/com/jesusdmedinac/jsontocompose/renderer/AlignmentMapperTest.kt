package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.ui.Alignment
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.exception.AlignmentException
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toAlignment
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toHorizontalsAlignment
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toVerticalAlignment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AlignmentMapperTest {

    // --- Scenario 1: toAlignment maps all 9 two-dimensional alignments ---

    @Test
    fun toAlignmentMapsAllNineValues() {
        assertEquals(Alignment.TopStart, "TopStart".toAlignment())
        assertEquals(Alignment.TopCenter, "TopCenter".toAlignment())
        assertEquals(Alignment.TopEnd, "TopEnd".toAlignment())
        assertEquals(Alignment.CenterStart, "CenterStart".toAlignment())
        assertEquals(Alignment.Center, "Center".toAlignment())
        assertEquals(Alignment.CenterEnd, "CenterEnd".toAlignment())
        assertEquals(Alignment.BottomStart, "BottomStart".toAlignment())
        assertEquals(Alignment.BottomCenter, "BottomCenter".toAlignment())
        assertEquals(Alignment.BottomEnd, "BottomEnd".toAlignment())
    }

    // --- Scenario 2: toAlignment throws AlignmentException for invalid value ---

    @Test
    fun toAlignmentThrowsForInvalidValue() {
        val exception = assertFailsWith<AlignmentException> {
            "InvalidAlignment".toAlignment()
        }
        assertTrue(exception.message!!.contains("InvalidAlignment"))
    }

    // --- Scenario 3: toVerticalAlignment maps all 3 vertical alignments ---

    @Test
    fun toVerticalAlignmentMapsAllThreeValues() {
        assertEquals(Alignment.Top, "Top".toVerticalAlignment())
        assertEquals(Alignment.CenterVertically, "CenterVertically".toVerticalAlignment())
        assertEquals(Alignment.Bottom, "Bottom".toVerticalAlignment())
    }

    // --- Scenario 4: toVerticalAlignment throws AlignmentException for invalid value ---

    @Test
    fun toVerticalAlignmentThrowsForInvalidValue() {
        assertFailsWith<AlignmentException> {
            "Left".toVerticalAlignment()
        }
    }

    // --- Scenario 5: toHorizontalsAlignment maps all 3 horizontal alignments ---

    @Test
    fun toHorizontalsAlignmentMapsAllThreeValues() {
        assertEquals(Alignment.Start, "Start".toHorizontalsAlignment())
        assertEquals(Alignment.CenterHorizontally, "CenterHorizontally".toHorizontalsAlignment())
        assertEquals(Alignment.End, "End".toHorizontalsAlignment())
    }

    // --- Scenario 6: toHorizontalsAlignment throws AlignmentException for invalid value ---

    @Test
    fun toHorizontalsAlignmentThrowsForInvalidValue() {
        assertFailsWith<AlignmentException> {
            "Top".toHorizontalsAlignment()
        }
    }
}
