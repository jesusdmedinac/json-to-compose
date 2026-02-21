package com.jesusdmedinac.jsontocompose.runtime

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.jesusdmedinac.jsontocompose.state.MutableStateHost
import com.jesusdmedinac.jsontocompose.state.StateHost
import com.jesusdmedinac.jsontocompose.model.ComposeAction
import com.jesusdmedinac.jsontocompose.model.ComposeDocument
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class CustomActionHandlersTest {

    // --- Scenario 1: LocalCustomActionHandlers CompositionLocal accepts a handler map ---

    @Test
    fun localCustomActionHandlersAcceptsMultipleHandlers() = runComposeUiTest {
        var navigateCalled = false
        var analyticsCalled = false

        val doc = ComposeDocument(
            actions = mapOf(
                "go" to listOf(
                    ComposeAction.Custom(customType = "navigate", params = buildJsonObject {
                        put("route", JsonPrimitive("home"))
                    }),
                ),
                "track" to listOf(
                    ComposeAction.Custom(customType = "analytics", params = buildJsonObject {
                        put("event", JsonPrimitive("page_view"))
                    }),
                ),
            ),
            root = ComposeNode(
                type = ComposeType.Column,
                properties = NodeProperties.ColumnProps(
                    children = listOf(
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                onClickEventName = "go",
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Navigate"),
                                ),
                            ),
                        ),
                        ComposeNode(
                            type = ComposeType.Button,
                            properties = NodeProperties.ButtonProps(
                                onClickEventName = "track",
                                child = ComposeNode(
                                    type = ComposeType.Text,
                                    properties = NodeProperties.TextProps(text = "Track"),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )

        setContent {
            CompositionLocalProvider(
                LocalCustomActionHandlers provides mapOf(
                    "navigate" to { navigateCalled = true },
                    "analytics" to { analyticsCalled = true },
                )
            ) {
                doc.ToCompose()
            }
        }

        onNodeWithText("Navigate").performClick()
        assertTrue(navigateCalled, "navigate handler should have been called")

        onNodeWithText("Track").performClick()
        assertTrue(analyticsCalled, "analytics handler should have been called")
    }

    // --- Scenario 2: Custom action type is dispatched to its registered handler ---

    @Test
    fun customActionDispatchedToRegisteredHandler() = runComposeUiTest {
        var receivedRoute: String? = null

        val doc = ComposeDocument(
            actions = mapOf(
                "go_home" to listOf(
                    ComposeAction.Custom(
                        customType = "navigate",
                        params = buildJsonObject {
                            put("route", JsonPrimitive("home"))
                        },
                    ),
                ),
            ),
            root = ComposeNode(
                type = ComposeType.Button,
                properties = NodeProperties.ButtonProps(
                    onClickEventName = "go_home",
                    child = ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = "Home"),
                    ),
                ),
            ),
        )

        setContent {
            CompositionLocalProvider(
                LocalCustomActionHandlers provides mapOf(
                    "navigate" to { action ->
                        receivedRoute = (action.params["route"] as? JsonPrimitive)?.content
                    },
                )
            ) {
                doc.ToCompose()
            }
        }

        onNodeWithText("Home").performClick()
        assertEquals("home", receivedRoute)
    }

    // --- Scenario 3: Custom action with unregistered type logs a warning ---

    @Test
    fun unregisteredCustomActionLogsWarningAndContinues() {
        val counterHost = MutableStateHost(0)
        val warnings = mutableListOf<String>()
        val dispatcher = ActionDispatcher(
            stateHosts = mapOf<String, StateHost<*>>("counter" to counterHost),
            customActionHandlers = emptyMap(),
            logger = { warnings.add(it) },
        )

        // Sequence: unknown custom action followed by a SetState
        dispatcher.dispatch(
            ComposeAction.Sequence(
                actions = listOf(
                    ComposeAction.Custom(customType = "sendEmail", params = buildJsonObject {
                        put("to", JsonPrimitive("test@example.com"))
                    }),
                    ComposeAction.SetState(stateKey = "counter", value = JsonPrimitive(42)),
                )
            )
        )

        // Warning was logged for the unregistered type
        assertEquals(1, warnings.size)
        assertTrue(warnings[0].contains("sendEmail"))

        // Execution continued — SetState still ran
        assertEquals(42, counterHost.state)
    }

    // --- Scenario 4: Custom action handler receives action params map ---

    @Test
    fun customActionHandlerReceivesFullParams() {
        var receivedParams: Map<String, Any?>? = null
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            customActionHandlers = mapOf(
                "httpRequest" to { action ->
                    receivedParams = action.params.mapValues { (_, v) ->
                        (v as? JsonPrimitive)?.content
                    }
                },
            ),
        )

        dispatcher.dispatch(
            ComposeAction.Custom(
                customType = "httpRequest",
                params = buildJsonObject {
                    put("url", JsonPrimitive("/api/data"))
                    put("method", JsonPrimitive("POST"))
                },
            )
        )

        assertEquals(2, receivedParams?.size)
        assertEquals("/api/data", receivedParams?.get("url"))
        assertEquals("POST", receivedParams?.get("method"))
    }
}
