package com.example.core

// GIVEN (read it, do not change it)
sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    sealed interface Failure : AppResult<Nothing> {
        data object NoInternet : Failure // no signal, or the host was not found
        data object Timeout : Failure // the server was too slow
        data object WrongLogin : Failure // the email or the password is wrong
        data object EmailTaken : Failure // an account already uses this email
        data class Unknown(val msg: String?) : Failure
    }
}
