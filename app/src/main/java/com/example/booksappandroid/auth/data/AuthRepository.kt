package com.example.booksappandroid.auth.data

import com.example.booksappandroid.auth.data.remote.RemoteAuthDataSource
import com.example.booksappandroid.core.Api
import com.example.booksappandroid.core.Result


object AuthRepository {
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = RemoteAuthDataSource.login(user)
        if (result is Result.Success<TokenHolder>) {
            setLoggedInUser(user, result.data)
        }
        return result
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        AuthRepository.user = user
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
