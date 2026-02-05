package com.jesusdmedinac.compose.sdui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jesusdmedinac.jsontocompose.behavior.Behavior
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.MutableStateHost
import com.jesusdmedinac.jsontocompose.com.jesusdmedinac.jsontocompose.state.StateHost

data class ShowcaseState(
    val stateHosts: Map<String, StateHost<*>>,
    val behaviors: Map<String, Behavior>
)

@Composable
fun rememberShowcaseState(): ShowcaseState {
    // Interactive StateHosts
    val textFieldHost = remember { MutableStateHost("") }
    val switchStateHost = remember { MutableStateHost(false) }
    val checkboxStateHost = remember { MutableStateHost(false) }
    val dialogVisibleHost = remember { MutableStateHost(false) }
    val switchEnabledHost = remember { MutableStateHost(true) }
    val checkboxEnabledHost = remember { MutableStateHost(true) }

    // BottomBar Selection StateHosts
    val navHomeSelected = remember { MutableStateHost(true) }
    val navSearchSelected = remember { MutableStateHost(false) }
    val navProfileSelected = remember { MutableStateHost(false) }

    val behaviors = remember {
        mapOf(
            // Interactive Components
            "onButtonClick" to object : Behavior {
                override fun invoke() {
                    println("Button Clicked")
                }
            },
            "onSwitchToggle" to object : Behavior {
                override fun invoke() {
                    println("Switch Toggled: ${switchStateHost.state}")
                }
            },
            "onCheckboxToggle" to object : Behavior {
                override fun invoke() {
                    println("Checkbox Toggled: ${checkboxStateHost.state}")
                }
            },
            "onShowDialog" to object : Behavior {
                override fun invoke() {
                    dialogVisibleHost.onStateChange(true)
                }
            },
            "onDialogConfirm" to object : Behavior {
                override fun invoke() {
                    println("Dialog Confirmed")
                    dialogVisibleHost.onStateChange(false)
                }
            },
            "onDialogDismiss" to object : Behavior {
                override fun invoke() {
                    println("Dialog Dismissed")
                    dialogVisibleHost.onStateChange(false)
                }
            },
            // Navigation Mutual Exclusion
            "onNavHome" to object : Behavior {
                override fun invoke() {
                    navHomeSelected.onStateChange(true)
                    navSearchSelected.onStateChange(false)
                    navProfileSelected.onStateChange(false)
                }
            },
            "onNavSearch" to object : Behavior {
                override fun invoke() {
                    navHomeSelected.onStateChange(false)
                    navSearchSelected.onStateChange(true)
                    navProfileSelected.onStateChange(false)
                }
            },
            "onNavProfile" to object : Behavior {
                override fun invoke() {
                    navHomeSelected.onStateChange(false)
                    navSearchSelected.onStateChange(false)
                    navProfileSelected.onStateChange(true)
                }
            }
        )
    }

    val stateHosts = remember {
        mapOf<String, StateHost<*>>(
            "text_field_value" to textFieldHost,
            "switch_state" to switchStateHost,
            "checkbox_state" to checkboxStateHost,
            "dialog_visible" to dialogVisibleHost,
            "switch_enabled" to switchEnabledHost,
            "checkbox_enabled" to checkboxEnabledHost,
            "nav_home_selected" to navHomeSelected,
            "nav_search_selected" to navSearchSelected,
            "nav_profile_selected" to navProfileSelected
        )
    }

    return remember(stateHosts, behaviors) {
        ShowcaseState(stateHosts, behaviors)
    }
}
