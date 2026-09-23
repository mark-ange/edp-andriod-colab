package com.example.ui

import com.example.domain.model.User

// GIVEN (read it, do not change it)
sealed interface AuthUiState {
    data object Idle : AuthUiState // nothing is happening yet
    data object Loading : AuthUiState // waiting for the server
    data class Error(val message: String) : AuthUiState // show this in red
    data class AccountCreated(val name: String) : AuthUiState // show this in green
    data class LoggedIn(val user: User) : AuthUiState // show the Profile screen
}
