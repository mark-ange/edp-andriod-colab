package com.example.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.AppResult
import com.example.data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    private val datePattern = Regex("""\d{4}-\d{2}-\d{2}""") // like 2004-05-17

    fun clearMessage() { uiState = AuthUiState.Idle }
    fun logout() { uiState = AuthUiState.Idle }

    private fun messageFor(failure: AppResult.Failure): String = when (failure) {
        AppResult.Failure.NoInternet -> "No internet connection. Please try again."
        AppResult.Failure.Timeout -> "The server was too slow. Please try again."
        AppResult.Failure.WrongLogin -> "Wrong email or password."
        AppResult.Failure.EmailTaken -> "An account with this email already exists."
        is AppResult.Failure.Unknown -> "Something went wrong: ${failure.msg}"
    }

    // TODO 8: login
    fun login(email: String, password: String) {
        // 8a: Validation
        if (email.isBlank() || password.isBlank()) {
            uiState = AuthUiState.Error("Please enter your email and password.")
            return
        }

        // 8b: Loading state
        uiState = AuthUiState.Loading

        // 8c: Launch coroutine
        viewModelScope.launch {
            uiState = when (val result = repository.login(email, password)) {
                is AppResult.Success -> AuthUiState.LoggedIn(result.data)
                is AppResult.Failure -> AuthUiState.Error(messageFor(result))
            }
        }
    }

    // TODO 9: register
    fun register(fullName: String, email: String, password: String, birthdate: String) {
        // 9a: Check all fields non-blank
        if (fullName.isBlank() || email.isBlank() || password.isBlank() || birthdate.isBlank()) {
            uiState = AuthUiState.Error("Please fill in all four fields.")
            return
        }

        // 9b: Email format check
        if (!email.contains("@")) {
            uiState = AuthUiState.Error("Please enter a valid email.")
            return
        }

        // 9c: Password length check
        if (password.length < 6) {
            uiState = AuthUiState.Error("Password must be at least 6 characters.")
            return
        }

        // 9d: Birthdate pattern check
        if (!datePattern.matches(birthdate.trim())) {
            uiState = AuthUiState.Error("Birthdate must look like 2004-05-17.")
            return
        }

        // 9e: Loading state and call repository
        uiState = AuthUiState.Loading

        viewModelScope.launch {
            uiState = when (val result = repository.register(fullName, email, password, birthdate)) {
                is AppResult.Success -> AuthUiState.AccountCreated(result.data.fullName)
                is AppResult.Failure -> AuthUiState.Error(messageFor(result))
            }
        }
    }
}
