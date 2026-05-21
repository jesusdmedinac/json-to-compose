package com.jesusdmedinac.jsontocompose.runtime

import com.jesusdmedinac.jsontocompose.model.ComposeAction
import com.jesusdmedinac.jsontocompose.model.SnackbarDuration
import com.jesusdmedinac.jsontocompose.state.StateHost
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShowSnackbarActionDispatcherTest {

    @Test
    fun dispatchShowSnackbarCallsHandler() {
        var receivedAction: ComposeAction.ShowSnackbar? = null
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            snackbarHandler = { receivedAction = it }
        )

        val action = ComposeAction.ShowSnackbar(
            message = "Item saved",
            actionLabel = "Undo",
            duration = SnackbarDuration.Short,
            withDismissAction = false
        )

        dispatcher.dispatch(action)

        assertEquals("Item saved", receivedAction?.message)
        assertEquals("Undo", receivedAction?.actionLabel)
        assertEquals(SnackbarDuration.Short, receivedAction?.duration)
    }

    @Test
    fun dispatchShowSnackbarWithNoHandlerLogsWarning() {
        val warnings = mutableListOf<String>()
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            snackbarHandler = null,
            logger = { warnings.add(it) }
        )

        dispatcher.dispatch(ComposeAction.ShowSnackbar(message = "Hello"))

        assert(warnings.any { it.contains("No snackbarHandler registered") })
    }

    @Test
    fun sequenceActionWithShowSnackbarIsDispatched() {
        var called = false
        val dispatcher = ActionDispatcher(
            stateHosts = emptyMap(),
            snackbarHandler = { called = true }
        )

        val sequence = ComposeAction.Sequence(
            actions = listOf(ComposeAction.ShowSnackbar(message = "Done"))
        )

        dispatcher.dispatch(sequence)
        assert(called)
    }
}
