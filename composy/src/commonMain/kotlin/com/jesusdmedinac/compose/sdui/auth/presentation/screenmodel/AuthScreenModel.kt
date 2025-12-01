package com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jesusdmedinac.compose.sdui.auth.domain.AuthRepository
import com.jesusdmedinac.compose.sdui.auth.domain.exception.AuthException
import com.jesusdmedinac.compose.sdui.auth.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthScreenModel(
    private val authRepository: AuthRepository
) : ScreenModel, AuthBehavior {
    private val _state: MutableStateFlow<AuthScreenState> = MutableStateFlow(AuthScreenState.Idle())
    val state = _state.asStateFlow()
    override fun onLoad() {
        screenModelScope.launch {
            authRepository.getCurrentUser()?.let { userInfo ->
                _state.update {
                    AuthScreenState.Authenticated(
                        user = User(
                            id = userInfo.id,
                            email = userInfo.email,
                        )
                    )
                }
            }
                ?: run {
                    _state.update {
                        AuthScreenState.UnAuthenticated()
                    }
                }
        }
    }

    override fun onEmailChange(email: String) {
        _state.update { state ->
            when (state) {
                is AuthScreenState.UnAuthenticated -> state.copy(
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
                is AuthScreenState.UnAuthenticated -> state.copy(
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
                is AuthScreenState.UnAuthenticated -> state.copy(
                    passwordVisible = !state.passwordVisible
                )

                else -> state
            }
        }
    }

    override fun authenticate() {
        screenModelScope.launch {
            val currentState = state.value
            _state.update { state ->
                when (state) {
                    is AuthScreenState.UnAuthenticated -> state.copy(isLoading = true)
                    else -> state
                }
            }

            when (val state = state.value) {
                is AuthScreenState.UnAuthenticated -> {
                    if (state.haveAccount) {
                        authRepository.signIn(state.email, state.password)
                    } else {
                        authRepository.signUp(state.email, state.password)
                    }
                }

                else -> return@launch
            }
                .onSuccess {
                    if (currentState is AuthScreenState.UnAuthenticated && !currentState.haveAccount) {
                        _state.update { AuthScreenState.SignupCheckEmail() }
                    } else {
                        authRepository.getCurrentUser()?.let { userInfo ->
                            _state.update {
                                AuthScreenState.Authenticated(
                                    isLoading = false,
                                    user = User(
                                        id = userInfo.id,
                                        email = userInfo.email,
                                    )
                                )
                            }
                        }
                    }
                }
                .onFailure { throwable ->
                    catchThrowable(throwable)
                }
        }
    }

    override fun onSwitchClick() {
        _state.update { state ->
            when (state) {
                is AuthScreenState.UnAuthenticated -> state.copy(
                    haveAccount = !state.haveAccount
                )

                else -> state
            }
        }
    }

    override fun navigateToLogin() {
        _state.update {
            AuthScreenState.UnAuthenticated(
                haveAccount = true
            )
        }
    }

    override fun logout() {
        screenModelScope.launch {
            _state.update { state ->
                when (state) {
                    is AuthScreenState.Authenticated -> state.copy(isLoading = true)
                    is AuthScreenState.UnAuthenticated -> state.copy(isLoading = true)
                    else -> state
                }
            }
            runCatching {
                authRepository.signOut()
            }
                .onSuccess {
                    _state.update {
                        AuthScreenState.Idle()
                    }
                }
                .onFailure { throwable ->
                    _state.update { state ->
                        when (state) {
                            is AuthScreenState.Authenticated -> state.copy(
                                isLoading = false,
                                error = throwable.message
                            )

                            is AuthScreenState.UnAuthenticated -> state.copy(
                                isLoading = false,
                                error = throwable.message
                            )

                            else -> state
                        }
                    }
                }
        }
    }

    private fun catchThrowable(throwable: Throwable) {
        when {
            throwable is AuthException -> {
                _state.update {
                    AuthScreenState.LoginCheckEmail(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }
}

sealed class AuthScreenState(
    open val isLoading: Boolean = false,
    open val error: String? = null
) {
    data class Idle(
        override val isLoading: Boolean = false,
        override val error: String? = null
    ) : AuthScreenState()

    data class Authenticated(
        override val isLoading: Boolean = false,
        override val error: String? = null,
        val user: User? = null,
    ) : AuthScreenState()

    data class UnAuthenticated(
        override val isLoading: Boolean = false,
        override val error: String? = null,
        val haveAccount: Boolean = false,
        val email: String = "",
        val password: String = "",
        val isValidEmail: Boolean = false,
        val isValidPassword: Boolean = false,
        val passwordVisible: Boolean = false
    ) : AuthScreenState()

    data class SignupCheckEmail(
        override val isLoading: Boolean = false,
        override val error: String? = null,
    ) : AuthScreenState()

    data class LoginCheckEmail(
        override val isLoading: Boolean = false,
        override val error: String? = null,
    ) : AuthScreenState()
}

interface AuthBehavior {
    fun onLoad()
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onPasswordVisibilityChange()
    fun authenticate()
    fun onSwitchClick()
    fun navigateToLogin()
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

            override fun navigateToLogin() {
                TODO("navigateToLogin is not yet implemented")
            }

            override fun logout() {
                TODO("logout is not yet implemented")
            }
        }
    }
}
