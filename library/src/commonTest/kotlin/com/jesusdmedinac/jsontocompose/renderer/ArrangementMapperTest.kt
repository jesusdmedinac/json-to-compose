package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.layout.Arrangement
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.exception.ArrangementException
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toArrangement
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toHorizontalArrangement
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.renderer.toVerticalArrangement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ArrangementMapperTest {

    // --- Scenario 1: toArrangement maps all 4 generic arrangements ---

    @Test
    fun toArrangementMapsAllFourValues() {
        assertEquals(Arrangement.Center, "Center".toArrangement())
        assertEquals(Arrangement.SpaceEvenly, "SpaceEvenly".toArrangement())
        assertEquals(Arrangement.SpaceBetween, "SpaceBetween".toArrangement())
        assertEquals(Arrangement.SpaceAround, "SpaceAround".toArrangement())
    }

    // --- Scenario 2: toArrangement throws ArrangementException for invalid value ---

    @Test
    fun toArrangementThrowsForInvalidValue() {
        val exception = assertFailsWith<ArrangementException> {
            "InvalidArrangement".toArrangement()
        }
        assertTrue(exception.message!!.contains("InvalidArrangement"))
    }

    // --- Scenario 3: toHorizontalArrangement maps all 6 standard arrangements ---

    @Test
    fun toHorizontalArrangementMapsAllSixStandardValues() {
        assertEquals(Arrangement.Start, "Start".toHorizontalArrangement())
        assertEquals(Arrangement.End, "End".toHorizontalArrangement())
        assertEquals(Arrangement.Center, "Center".toHorizontalArrangement())
        assertEquals(Arrangement.SpaceEvenly, "SpaceEvenly".toHorizontalArrangement())
        assertEquals(Arrangement.SpaceBetween, "SpaceBetween".toHorizontalArrangement())
        assertEquals(Arrangement.SpaceAround, "SpaceAround".toHorizontalArrangement())
    }

    // --- Scenario 4: toHorizontalArrangement maps all 6 absolute arrangements ---

    @Test
    fun toHorizontalArrangementMapsAllSixAbsoluteValues() {
        assertEquals(Arrangement.Absolute.Left, "AbsoluteLeft".toHorizontalArrangement())
        assertEquals(Arrangement.Absolute.Center, "AbsoluteCenter".toHorizontalArrangement())
        assertEquals(Arrangement.Absolute.Right, "AbsoluteRight".toHorizontalArrangement())
        assertEquals(Arrangement.Absolute.SpaceBetween, "AbsoluteSpaceBetween".toHorizontalArrangement())
        assertEquals(Arrangement.Absolute.SpaceEvenly, "AbsoluteSpaceEvenly".toHorizontalArrangement())
        assertEquals(Arrangement.Absolute.SpaceAround, "AbsoluteSpaceAround".toHorizontalArrangement())
    }

    // --- Scenario 5: toHorizontalArrangement throws ArrangementException for invalid value ---

    @Test
    fun toHorizontalArrangementThrowsForInvalidValue() {
        assertFailsWith<ArrangementException> {
            "Bottom".toHorizontalArrangement()
        }
    }

    // --- Scenario 6: toVerticalArrangement maps all 6 vertical arrangements ---

    @Test
    fun toVerticalArrangementMapsAllSixValues() {
        assertEquals(Arrangement.Top, "Top".toVerticalArrangement())
        assertEquals(Arrangement.Bottom, "Bottom".toVerticalArrangement())
        assertEquals(Arrangement.Center, "Center".toVerticalArrangement())
        assertEquals(Arrangement.SpaceEvenly, "SpaceEvenly".toVerticalArrangement())
        assertEquals(Arrangement.SpaceBetween, "SpaceBetween".toVerticalArrangement())
        assertEquals(Arrangement.SpaceAround, "SpaceAround".toVerticalArrangement())
    }

    // --- Scenario 7: toVerticalArrangement throws ArrangementException for invalid value ---

    @Test
    fun toVerticalArrangementThrowsForInvalidValue() {
        assertFailsWith<ArrangementException> {
            "Start".toVerticalArrangement()
        }
    }
}
