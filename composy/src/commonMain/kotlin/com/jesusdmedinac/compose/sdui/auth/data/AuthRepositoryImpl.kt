package com.jesusdmedinac.compose.sdui.auth.data

import com.jesusdmedinac.compose.sdui.auth.domain.AuthRepository
import com.jesusdmedinac.compose.sdui.auth.domain.model.User
import io.github.jan.supabase.auth.Auth
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

    override suspend fun signIn(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signUp(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
