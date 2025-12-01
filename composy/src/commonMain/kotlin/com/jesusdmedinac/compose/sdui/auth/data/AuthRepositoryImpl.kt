package com.jesusdmedinac.compose.sdui.auth.data

import com.jesusdmedinac.compose.sdui.auth.domain.AuthRepository
import com.jesusdmedinac.compose.sdui.auth.domain.exception.AuthException
import com.jesusdmedinac.compose.sdui.auth.domain.model.User
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepositoryImpl(
    private val auth: Auth
) : AuthRepository {
    override suspend fun getCurrentUser(): User? = auth.currentUserOrNull()?.let {
        User(
            id = it.id,
            email = it.email ?: ""
        )
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }
        .foldSupabaseResult()

    override suspend fun signUp(email: String, password: String): Result<Unit> = runCatching {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }
        .foldSupabaseResult()

    override suspend fun signOut(): Result<Unit> = runCatching {
        auth.signOut()
    }
        .foldSupabaseResult()

    private fun <T> Result<T>.foldSupabaseResult(): Result<Unit> = fold(
        onSuccess = { Result.success(Unit) },
        onFailure = { throwable ->
            Result.failure(
                when {
                    throwable is AuthRestException -> AuthException(throwable)
                    else -> throwable
                }
            )
        }
    )
}
