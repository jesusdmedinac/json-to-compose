package com.jesusdmedinac.jsontocompose.runtime

import com.jesusdmedinac.jsontocompose.model.ComposeAction
import com.jesusdmedinac.jsontocompose.model.ConditionOperator
import com.jesusdmedinac.jsontocompose.model.ListOperation
import com.jesusdmedinac.jsontocompose.state.MutableStateHost
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdvancedActionsTest {

    // --- 1. IncrementState & DecrementState ---

    @Test
    fun incrementStateIncrementsInt() {
        val counterHost = MutableStateHost(5)
        val stateHosts = mapOf<String, StateHost<*>>("counter" to counterHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.IncrementState(stateKey = "counter", by = 2.0))

        assertEquals(7, counterHost.state)
    }

    @Test
    fun incrementStateIncrementsDouble() {
        val valueHost = MutableStateHost(5.5)
        val stateHosts = mapOf<String, StateHost<*>>("val" to valueHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.IncrementState(stateKey = "val", by = 1.5))

        assertEquals(7.0, valueHost.state)
    }

    @Test
    fun incrementStateIncrementsFloat() {
        val valueHost = MutableStateHost(5.5f)
        val stateHosts = mapOf<String, StateHost<*>>("val" to valueHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.IncrementState(stateKey = "val", by = 1.5))

        assertEquals(7.0f, valueHost.state)
    }

    @Test
    fun incrementStateIncrementsLong() {
        val valueHost = MutableStateHost(5L)
        val stateHosts = mapOf<String, StateHost<*>>("val" to valueHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.IncrementState(stateKey = "val", by = 2.0))

        assertEquals(7L, valueHost.state)
    }

    @Test
    fun decrementStateDecrementsInt() {
        val counterHost = MutableStateHost(10)
        val stateHosts = mapOf<String, StateHost<*>>("counter" to counterHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(ComposeAction.DecrementState(stateKey = "counter", by = 3.0))

        assertEquals(7, counterHost.state)
    }

    // --- 2. Conditional Evaluation ---

    @Test
    fun conditionalExecutesThenBranchWhenEquals() {
        val nameHost = MutableStateHost("Antigravity")
        val resultHost = MutableStateHost("initial")
        val stateHosts = mapOf<String, StateHost<*>>(
            "name" to nameHost,
            "result" to resultHost
        )
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        val conditionalAction = ComposeAction.Conditional(
            stateKey = "name",
            operator = ConditionOperator.Equals,
            value = JsonPrimitive("Antigravity"),
            thenAction = ComposeAction.SetState("result", JsonPrimitive("matched")),
            elseAction = ComposeAction.SetState("result", JsonPrimitive("failed"))
        )

        dispatcher.dispatch(conditionalAction)
        assertEquals("matched", resultHost.state)
    }

    @Test
    fun conditionalExecutesThenBranchWhenEqualsHeterogeneousNumeric() {
        val valueHost = MutableStateHost(5)
        val resultHost = MutableStateHost("initial")
        val stateHosts = mapOf<String, StateHost<*>>(
            "val" to valueHost,
            "result" to resultHost
        )
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        val conditionalAction = ComposeAction.Conditional(
            stateKey = "val",
            operator = ConditionOperator.Equals,
            value = JsonPrimitive(5.0),
            thenAction = ComposeAction.SetState("result", JsonPrimitive("matched")),
            elseAction = ComposeAction.SetState("result", JsonPrimitive("failed"))
        )

        dispatcher.dispatch(conditionalAction)
        assertEquals("matched", resultHost.state)
    }

    @Test
    fun conditionalExecutesElseBranchWhenNotEquals() {
        val nameHost = MutableStateHost("Antigravity")
        val resultHost = MutableStateHost("initial")
        val stateHosts = mapOf<String, StateHost<*>>(
            "name" to nameHost,
            "result" to resultHost
        )
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        val conditionalAction = ComposeAction.Conditional(
            stateKey = "name",
            operator = ConditionOperator.NotEquals,
            value = JsonPrimitive("Antigravity"),
            thenAction = ComposeAction.SetState("result", JsonPrimitive("matched")),
            elseAction = ComposeAction.SetState("result", JsonPrimitive("failed"))
        )

        dispatcher.dispatch(conditionalAction)
        assertEquals("failed", resultHost.state)
    }

    @Test
    fun conditionalEvaluatesGreaterThanCorrectly() {
        val ageHost = MutableStateHost(25)
        val resultHost = MutableStateHost(false)
        val stateHosts = mapOf<String, StateHost<*>>(
            "age" to ageHost,
            "result" to resultHost
        )
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        val conditionalAction = ComposeAction.Conditional(
            stateKey = "age",
            operator = ConditionOperator.GreaterThan,
            value = JsonPrimitive(18),
            thenAction = ComposeAction.SetState("result", JsonPrimitive(true))
        )

        dispatcher.dispatch(conditionalAction)
        assertEquals(true, resultHost.state)
    }

    @Test
    fun conditionalEvaluatesContainsCorrectly() {
        val bioHost = MutableStateHost("Software engineer in mobile SDUI")
        val resultHost = MutableStateHost(false)
        val stateHosts = mapOf<String, StateHost<*>>(
            "bio" to bioHost,
            "result" to resultHost
        )
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        val conditionalAction = ComposeAction.Conditional(
            stateKey = "bio",
            operator = ConditionOperator.Contains,
            value = JsonPrimitive("mobile"),
            thenAction = ComposeAction.SetState("result", JsonPrimitive(true))
        )

        dispatcher.dispatch(conditionalAction)
        assertEquals(true, resultHost.state)
    }

    // --- 3. UpdateList (Add & Remove) ---

    @Test
    fun updateListAddsItem() {
        val listHost = MutableStateHost(listOf("apple", "banana"))
        val stateHosts = mapOf<String, StateHost<*>>("fruits" to listHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(
            ComposeAction.UpdateList(
                stateKey = "fruits",
                operation = ListOperation.Add,
                item = JsonPrimitive("cherry")
            )
        )

        assertEquals(listOf("apple", "banana", "cherry"), listHost.state)
    }

    @Test
    fun updateListRemovesItem() {
        val listHost = MutableStateHost(listOf("apple", "banana", "cherry"))
        val stateHosts = mapOf<String, StateHost<*>>("fruits" to listHost)
        val dispatcher = ActionDispatcher(stateHosts = stateHosts)

        dispatcher.dispatch(
            ComposeAction.UpdateList(
                stateKey = "fruits",
                operation = ListOperation.Remove,
                item = JsonPrimitive("banana")
            )
        )

        assertEquals(listOf("apple", "cherry"), listHost.state)
    }

    // --- 4. Navigation & Platform Handlers ---

    @Test
    fun navigateForwardsToNavigationHandler() {
        var routedTo: String? = null
        var routeArgs: Map<String, Any?>? = null
        val navHandler = object : NavigationHandler {
            override fun navigate(route: String, args: Map<String, Any?>) {
                routedTo = route
                routeArgs = args
            }
            override fun navigateBack() {}
        }
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            navigationHandler = navHandler
        )

        dispatcher.dispatch(
            ComposeAction.Navigate(
                route = "/profile",
                args = mapOf("userId" to JsonPrimitive("user_123"))
            )
        )

        assertEquals("/profile", routedTo)
        assertEquals(mapOf("userId" to "user_123"), routeArgs)
    }

    @Test
    fun navigateBackForwardsToNavigationHandler() {
        var popped = false
        val navHandler = object : NavigationHandler {
            override fun navigate(route: String, args: Map<String, Any?>) {}
            override fun navigateBack() {
                popped = true
            }
        }
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            navigationHandler = navHandler
        )

        dispatcher.dispatch(ComposeAction.NavigateBack)

        assertTrue(popped)
    }

    @Test
    fun copyToClipboardForwardsToPlatformHandler() {
        var copiedText: String? = null
        val platHandler = object : PlatformHandler {
            override fun launchUrl(url: String) {}
            override fun copyToClipboard(text: String) {
                copiedText = text
            }
        }
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            platformHandler = platHandler
        )

        dispatcher.dispatch(ComposeAction.CopyToClipboard(text = "Hello Antigravity"))

        assertEquals("Hello Antigravity", copiedText)
    }

    // --- 5. Asynchronous Delay Support ---

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delayActionSuspendsExecution() = runTest {
        val resultHost = MutableStateHost("initial")
        val stateHosts = mapOf<String, StateHost<*>>("result" to resultHost)
        
        // Construct dispatcher with this test's CoroutineScope (which supports virtual time)
        val dispatcher = ActionDispatcher(
            stateHosts = stateHosts,
            coroutineScope = this
        )

        val sequenceWithDelay = ComposeAction.Sequence(
            actions = listOf(
                ComposeAction.SetState("result", JsonPrimitive("first")),
                ComposeAction.Delay(durationMillis = 1000L),
                ComposeAction.SetState("result", JsonPrimitive("second"))
            )
        )

        // Dispatch launches asynchronous coroutine
        dispatcher.dispatch(sequenceWithDelay)

        // Execute coroutine up to suspension point
        testScheduler.runCurrent()

        // Under virtual time, execution hasn't completed yet, but first action has executed
        assertEquals("first", resultHost.state)

        // Advance virtual time by 1000ms
        testScheduler.advanceTimeBy(1000L)
        testScheduler.runCurrent()

        // After the delay, second action must have executed
        assertEquals("second", resultHost.state)
    }
}
