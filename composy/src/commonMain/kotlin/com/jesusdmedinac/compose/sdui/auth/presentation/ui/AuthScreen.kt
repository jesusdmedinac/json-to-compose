package com.jesusdmedinac.compose.sdui.auth.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthBehavior
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthScreenModel
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthState
import com.jesusdmedinac.compose.sdui.presentation.ui.MainScreen
import com.jesusdmedinac.compose.sdui.presentation.ui.theme.ComposyTheme

object AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val authScreenModel = koinScreenModel<AuthScreenModel>()
        val state by authScreenModel.state.collectAsState()
        val isLoading = state.isLoading

        LaunchedEffect(Unit) {
            authScreenModel.onLoad()
        }

        LaunchedEffect(state) {
            when (state) {
                is AuthState.Authenticated -> {
                    navigator.replace(MainScreen)
                }

                is AuthState.Idle,
                is AuthState.UnAuthenticated -> Unit
            }
        }

        if (isLoading) {
            Loading()
        } else {
            SignInOrSignUp(
                authState = state,
                authBehavior = authScreenModel
            )
        }
    }

    @Composable
    private fun SignInOrSignUp(
        authState: AuthState = AuthState.Idle(),
        authBehavior: AuthBehavior = AuthBehavior.Default
    ) {
        var emailTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

        val unAuthenticated = when (authState) {
            is AuthState.UnAuthenticated -> authState
            else -> AuthState.UnAuthenticated()
        }
        val haveAccount = unAuthenticated.haveAccount
        val error = unAuthenticated.error
        val isValidEmail = unAuthenticated.isValidEmail
        val isValidPassword = unAuthenticated.isValidPassword
        val passwordVisible = unAuthenticated.passwordVisible

        ComposyTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (haveAccount) "Sign in"
                        else "Sign up",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    OutlinedTextField(
                        value = emailTextFieldValue,
                        onValueChange = {
                            emailTextFieldValue = it
                            authBehavior.onEmailChange(it.text)
                        },
                        label = {
                            Text(
                                text = "Email"
                            )
                        }
                    )
                    OutlinedTextField(
                        value = passwordTextFieldValue,
                        onValueChange = {
                            passwordTextFieldValue = it
                            authBehavior.onPasswordChange(it.text)
                        },
                        label = {
                            Text(
                                text = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    authBehavior.onPasswordVisibilityChange()
                                },
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Lucide.Eye
                                    else Lucide.EyeOff,
                                    contentDescription = "Password visibility",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions = if (passwordVisible) KeyboardOptions.Default
                        else KeyboardOptions(keyboardType = KeyboardType.Password),
                    )
                    OutlinedButton(
                        onClick = {
                            authBehavior.authenticate()
                        },
                        enabled = isValidEmail && isValidPassword
                    ) {
                        Text(
                            text = if (haveAccount) "Sign in"
                            else "Sign up"
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (haveAccount) "Don't have an account?"
                            else "Already have an account?",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(
                            onClick = {
                                authBehavior.onSwitchClick()
                            }
                        ) {
                            Text(
                                text = if (haveAccount) "Sign up"
                                else "Login"
                            )
                        }
                        Text(
                            text = "free",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    @Composable
    private fun Loading() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                CircularProgressIndicator()
                Text("Loading...")
            }
        }
    }
}