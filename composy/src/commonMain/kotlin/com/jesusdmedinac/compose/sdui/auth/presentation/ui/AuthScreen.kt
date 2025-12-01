package com.jesusdmedinac.compose.sdui.auth.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
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
import com.jesusdmedinac.compose.sdui.auth.presentation.screenmodel.AuthScreenState
import com.jesusdmedinac.compose.sdui.presentation.ui.MainScreen
import com.jesusdmedinac.compose.sdui.presentation.ui.theme.ComposyTheme

object AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val authScreenModel = koinScreenModel<AuthScreenModel>()
        val state by authScreenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            authScreenModel.onLoad()
        }

        when (val currentState = state) {
            is AuthScreenState.Authenticated -> navigator.replace(MainScreen)
            is AuthScreenState.SignupCheckEmail -> CheckEmailScreen(
                authScreenState = currentState,
                authBehavior = authScreenModel
            )
            is AuthScreenState.LoginCheckEmail -> CheckEmailScreen(
                authScreenState = currentState,
                authBehavior = authScreenModel
            )

            is AuthScreenState.Idle, is AuthScreenState.UnAuthenticated -> if (currentState.isLoading) {
                Loading()
            } else {
                SignInOrSignUp(
                    authScreenModel = authScreenModel
                )
            }
        }
    }

    @Composable
    private fun CheckEmailScreen(
        authScreenState: AuthScreenState = AuthScreenState.Idle(),
        authBehavior: AuthBehavior
    ) {
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
                        text = when (authScreenState) {
                            is AuthScreenState.LoginCheckEmail -> "We have sent you an email!"
                            else -> "Sign up successful!"
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Please check your email to confirm your account.",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { authBehavior.navigateToLogin() }
                    ) {
                        Text("Go to Login")
                    }
                }
            }
        }
    }

    @Composable
    private fun SignInOrSignUp(
        authScreenModel: AuthScreenModel = koinScreenModel(),
    ) {
        val state by authScreenModel.state.collectAsState()
        val authState = state
        var emailTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

        val unAuthenticated = when (authState) {
            is AuthScreenState.UnAuthenticated -> authState
            else -> AuthScreenState.UnAuthenticated()
        }
        val haveAccount = unAuthenticated.haveAccount
        val error = unAuthenticated.error
        val isValidEmail = unAuthenticated.isValidEmail
        val isValidPassword = unAuthenticated.isValidPassword
        val passwordVisible = unAuthenticated.passwordVisible
        val focusManager = LocalFocusManager.current

        ComposyTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.onPreviewKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            when (event.key) {
                                Key.Tab -> {
                                    focusManager.moveFocus(FocusDirection.Next)
                                    return@onPreviewKeyEvent true
                                }
                                Key.Enter -> {
                                    if (isValidEmail && isValidPassword) {
                                        authScreenModel.authenticate()
                                        return@onPreviewKeyEvent true
                                    }
                                }
                            }
                        }
                        false
                    },
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
                            authScreenModel.onEmailChange(it.text)
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
                            authScreenModel.onPasswordChange(it.text)
                        },
                        label = {
                            Text(
                                text = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    authScreenModel.onPasswordVisibilityChange()
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
                            authScreenModel.authenticate()
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
                                authScreenModel.onSwitchClick()
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
