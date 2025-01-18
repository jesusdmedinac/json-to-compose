package com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthScreenModel(
    private val auth: Auth
) : ScreenModel, AuthBehavior {
    private val _state: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Idle())
    val state = _state.asStateFlow()
    override fun onLoad() {
        screenModelScope.launch {
            auth.currentUserOrNull()?.let { userInfo ->
                _state.update {
                    AuthState.Authenticated(
                        user = User(
                            id = userInfo.id,
                            email = userInfo.email ?: "",
                        )
                    )
                }
            }
                ?: run {
                    _state.update {
                        AuthState.UnAuthenticated()
                    }
                }
        }
    }

    override fun onEmailChange(email: String) {
        _state.update { state ->
            when (state) {
                is AuthState.UnAuthenticated -> state.copy(
                    email = email,
                    isValidEmail = email.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")),
                )

                else -> state
            }
        }
    }

    override fun onPasswordChange(password: String) {
        _state.update { state ->
            when (state) {
                is AuthState.UnAuthenticated -> state.copy(
                    password = password,
                    isValidPassword = password.length >= 8,
                )

                else -> state
            }
        }
    }

    override fun onPasswordVisibilityChange() {
        _state.update { state ->
            when (state) {
                is AuthState.UnAuthenticated -> state.copy(
                    passwordVisible = !state.passwordVisible
                )

                else -> state
            }
        }
    }

    override fun authenticate() {
        screenModelScope.launch {
            _state.update { state ->
                when (state) {
                    is AuthState.UnAuthenticated -> state.copy(isLoading = true)
                    else -> state
                }
            }
            runCatching {
                when (val state = state.value) {
                    is AuthState.UnAuthenticated -> {
                        if (state.haveAccount) {
                            auth.signInWith(Email) {
                                email = state.email
                                password = state.password
                            }
                        } else {
                            auth.signUpWith(Email) {
                                email = state.email
                                password = state.password
                            }
                        }
                    }

                    else -> return@launch
                }
            }
                .onSuccess {
                    auth.currentUserOrNull()?.let { userInfo ->
                        _state.update {
                            AuthState.Authenticated(
                                isLoading = false,
                                user = User(
                                    id = userInfo.id,
                                    email = userInfo.email ?: "",
                                )
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        AuthState.Idle(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }

    override fun onSwitchClick() {
        _state.update { state ->
            when (state) {
                is AuthState.UnAuthenticated -> state.copy(
                    haveAccount = !state.haveAccount
                )

                else -> state
            }
        }
    }

    override fun logout() {
        screenModelScope.launch {
            _state.update { state ->
                when (state) {
                    is AuthState.Authenticated -> state.copy(isLoading = true)
                    is AuthState.UnAuthenticated -> state.copy(isLoading = true)
                    else -> state
                }
            }
            runCatching {
                auth.signOut()
            }
                .onSuccess {
                    _state.update {
                        AuthState.Idle()
                    }
                }
                .onFailure {
                    _state.update { state ->
                        when (state) {
                            is AuthState.Authenticated -> state.copy(
                                isLoading = false,
                                error = it.message
                            )

                            is AuthState.UnAuthenticated -> state.copy(
                                isLoading = false,
                                error = it.message
                            )

                            else -> state
                        }
                    }
                }
        }
    }
}

sealed class AuthState(
    open val isLoading: Boolean = false,
    open val error: String? = null
) {
    data class Idle(
        override val isLoading: Boolean = false,
        override val error: String? = null
    ) : AuthState()

    data class Authenticated(
        override val isLoading: Boolean = false,
        override val error: String? = null,
        val user: User? = null,
    ) : AuthState()

    data class UnAuthenticated(
        override val isLoading: Boolean = false,
        override val error: String? = null,
        val haveAccount: Boolean = false,
        val email: String = "",
        val password: String = "",
        val isValidEmail: Boolean = false,
        val isValidPassword: Boolean = false,
        val passwordVisible: Boolean = false
    ) : AuthState()
}

interface AuthBehavior {
    fun onLoad()
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onPasswordVisibilityChange()
    fun authenticate()
    fun onSwitchClick()
    fun logout()

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

            override fun logout() {
                TODO("logout is not yet implemented")
            }
        }
    }
}

data class User(
    val id: String,
    val email: String,
)