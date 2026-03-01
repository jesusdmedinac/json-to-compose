package com.jesusdmedinac.compose.sdui.auth.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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
            is AuthScreenState.SignupCheckEmail, is AuthScreenState.LoginCheckEmail -> CheckEmailScreen(
                authBehavior = authScreenModel,
                isLogin = currentState is AuthScreenState.LoginCheckEmail
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
        authBehavior: AuthBehavior,
        isLogin: Boolean
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
                        text = if (isLogin) "We have sent you an email!" else "Sign up successful!",
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
        val unAuthenticated = when (val authState = state) {
            is AuthScreenState.UnAuthenticated -> authState
            else -> return // Should not happen in this branch
        }

        val focusManager = LocalFocusManager.current

        ComposyTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .onPreviewKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown) {
                                when (event.key) {
                                    Key.Tab -> {
                                        focusManager.moveFocus(if (event.isShiftPressed) FocusDirection.Previous else FocusDirection.Next)
                                        return@onPreviewKeyEvent true
                                    }
                                    Key.Enter -> {
                                        if (unAuthenticated.isValidEmail && unAuthenticated.isValidPassword) {
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
                    AuthHeader(isSignIn = unAuthenticated.haveAccount)
                    Spacer(Modifier.height(16.dp))
                    EmailField(
                        email = unAuthenticated.email,
                        onEmailChange = authScreenModel::onEmailChange
                    )
                    Spacer(Modifier.height(8.dp))
                    PasswordField(
                        password = unAuthenticated.password,
                        onPasswordChange = authScreenModel::onPasswordChange,
                        isPasswordVisible = unAuthenticated.passwordVisible,
                        onVisibilityChange = authScreenModel::onPasswordVisibilityChange
                    )
                    Spacer(Modifier.height(16.dp))
                    SubmitButton(
                        isSignIn = unAuthenticated.haveAccount,
                        isEnabled = unAuthenticated.isValidEmail && unAuthenticated.isValidPassword,
                        onClick = authScreenModel::authenticate
                    )
                    Spacer(Modifier.height(8.dp))
                    SwitchAuthModeRow(
                        isSignIn = unAuthenticated.haveAccount,
                        onSwitchClick = authScreenModel::onSwitchClick
                    )
                    ErrorText(error = unAuthenticated.error)
                }
            }
        }
    }

    @Composable
    private fun AuthHeader(isSignIn: Boolean) {
        Text(
            text = if (isSignIn) "Sign in" else "Sign up",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium
        )
    }

    @Composable
    private fun EmailField(email: String, onEmailChange: (String) -> Unit) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChange,
            label = { Text(text = "Email") },
            singleLine = true
        )
    }

    @Composable
    private fun PasswordField(
        password: String,
        onPasswordChange: (String) -> Unit,
        isPasswordVisible: Boolean,
        onVisibilityChange: () -> Unit
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(text = "Password") },
            trailingIcon = {
                IconButton(onClick = onVisibilityChange) {
                    Icon(
                        imageVector = if (isPasswordVisible) Lucide.Eye else Lucide.EyeOff,
                        contentDescription = "Password visibility",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = if (isPasswordVisible) KeyboardType.Text else KeyboardType.Password),
            singleLine = true
        )
    }

    @Composable
    private fun SubmitButton(isSignIn: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,

            enabled = isEnabled
        ) {
            Text(text = if (isSignIn) "Sign in" else "Sign up")
        }
    }

    @Composable
    private fun SwitchAuthModeRow(isSignIn: Boolean, onSwitchClick: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSignIn) "Don't have an account?" else "Already have an account?",
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = onSwitchClick) {
                Text(text = if (isSignIn) "Sign up" else "Login")
            }
            Text(
                text = "free",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    @Composable
    private fun ErrorText(error: String?) {
        if (!error.isNullOrBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
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
