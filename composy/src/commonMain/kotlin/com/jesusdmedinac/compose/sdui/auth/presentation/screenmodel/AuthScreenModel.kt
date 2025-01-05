package com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthScreenModel(
    private val auth: Auth
) : ScreenModel, AuthBehavior {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()
    override fun onLoad() {
        screenModelScope.launch {
            auth.currentUserOrNull()?.let {
                _state.update { state ->
                    state.copy(
                        user = User(
                            id = it.id,
                            email = it.email ?: "",
                        )
                    )
                }
            }
        }
    }

    override fun onEmailChange(email: String) {
        _state.update { state ->
            state.copy(
                email = email,
                isValidEmail = email.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")),
            )
        }
    }

    override fun onPasswordChange(password: String) {
        _state.update { state ->
            state.copy(
                password = password,
                isValidPassword = password.length >= 8,
            )
        }
    }

    override fun onPasswordVisibilityChange() {
        _state.update { state ->
            state.copy(
                passwordVisible = !state.passwordVisible
            )
        }
    }

    override fun authenticate() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            runCatching {
                if (state.value.haveAccount) {
                    auth.signInWith(Email) {
                        email = state.value.email
                        password = state.value.password
                    }
                } else {
                    auth.signUpWith(Email) {
                        email = state.value.email
                        password = state.value.password
                    }
                }
            }
                .onSuccess {
                    auth.currentUserOrNull()?.let {
                        _state.update { state ->
                            state.copy(
                                user = User(
                                    id = it.id,
                                    email = it.email ?: "",
                                )
                            )
                        }
                    }
                }
                .onFailure {
                    _state.update { state ->
                        state.copy(
                            error = it.message
                        )
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    override fun onSwitchClick() {
        _state.update { state ->
            state.copy(
                haveAccount = !state.haveAccount
            )
        }
    }
}

data class AuthState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isValidEmail: Boolean = false,
    val isValidPassword: Boolean = false,
    val haveAccount: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val passwordVisible: Boolean = false
)

interface AuthBehavior {
    fun onLoad()
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onPasswordVisibilityChange()
    fun authenticate()
    fun onSwitchClick()

    companion object {
        val Default = object : AuthBehavior {
            override fun onLoad() {
                TODO("onLoad is not yet implemented")
            }

            override fun onEmailChange(email: String) {
                TODO("onEmailChange is not yet implemented")
            }

            override fun onPasswordChange(password: String) {
                TODO("onPasswordChange is not yet implemented")
            }

            override fun onPasswordVisibilityChange() {
                TODO("onPasswordVisibilityChange is not yet implemented")
            }

            override fun authenticate() {
                TODO("authenticate is not yet implemented")
            }

            override fun onSwitchClick() {
                TODO("onSwitchClick is not yet implemented")
            }
        }
    }
}

data class User(
    val id: String,
    val email: String,
)