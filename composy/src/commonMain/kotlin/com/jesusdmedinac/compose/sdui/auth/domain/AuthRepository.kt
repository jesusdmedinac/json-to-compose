package com.jesusdmedinac.compose.sdui.auth.domain

import com.jesusdmedinac.compose.sdui.auth.domain.model.User

interface AuthRepository {
    suspend fun getCurrentUser(): User?
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
}
