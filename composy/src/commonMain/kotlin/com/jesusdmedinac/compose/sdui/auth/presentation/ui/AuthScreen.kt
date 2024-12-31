package com.jesusdmedinac.compose.sdui.auth.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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
        val isLoggedIn = state.user != null
        val isLoading = state.isLoading

        LaunchedEffect(Unit) {
            authScreenModel.onLoad()
        }

        LaunchedEffect(isLoggedIn) {
            if (isLoggedIn) {
                navigator.replace(MainScreen)
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
        authState: AuthState = AuthState(),
        authBehavior: AuthBehavior = AuthBehavior.Default
    ) {
        var emailTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

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
                        text = if (authState.haveAccount) "Sign in"
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
                        }
                    )
                    OutlinedButton(
                        onClick = {
                            authBehavior.authenticate()
                        },
                        enabled = authState.isValidEmail && authState.isValidPassword
                    ) {
                        Text(
                            text = if (authState.haveAccount) "Sign in"
                            else "Sign up"
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (authState.haveAccount) "Don't have an account?"
                            else "Already have an account?",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(
                            onClick = {
                                authBehavior.onSwitchClick()
                            }
                        ) {
                            Text(
                                text = if (authState.haveAccount) "Sign up"
                                else "Login"
                            )
                        }
                        Text(
                            text = "free",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = authState.error ?: "",
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