package com.example.myapplication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.core.AppResult
import com.example.myapplication.data.local.ChatDatabase
import com.example.myapplication.data.network.NetworkModule
import com.example.myapplication.data.repository.ChatRepositoryImpl
import com.example.myapplication.domain.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    var uiState: ChatUiState by mutableStateOf(ChatUiState.Loading)
        private set

    var myName: String by mutableStateOf("MARK ANGELOU ABELLANO")
        private set

    var draft: String by mutableStateOf("")
        private set

    fun onNameChange(value: String) {
        myName = value
    }

    fun onDraftChange(value: String) {
        draft = value
    }

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            uiState = ChatUiState.Loading
            uiState = when (val result = repository.getMessages()) {
                is AppResult.Success -> {
                    if (result.data.isEmpty()) ChatUiState.Empty else ChatUiState.Ready(result.data)
                }
                AppResult.Failure.NoInternet -> ChatUiState.Error("No internet connection.")
                AppResult.Failure.Timeout -> ChatUiState.Error("The server took too long.")
                is AppResult.Failure.Unknown -> ChatUiState.Error(result.message ?: "Something went wrong.")
            }
        }
    }

    fun send() {
        if (myName.isBlank() || draft.isBlank()) return
        viewModelScope.launch {
            val result = repository.sendMessage(myName, draft)
            if (result is AppResult.Success) {
                draft = ""
                load()
            } else if (result is AppResult.Failure.Unknown && result.message?.contains("Max number of elements") == true) {
                // Auto-pruning logic
                val messagesResult = repository.getMessages()
                if (messagesResult is AppResult.Success && messagesResult.data.isNotEmpty()) {
                    val oldestId = messagesResult.data.last().id
                    repository.deleteMessage(oldestId)
                    if (repository.sendMessage(myName, draft) is AppResult.Success) {
                        draft = ""
                        load()
                        return@launch
                    }
                }
                uiState = ChatUiState.Error("Server full. Try deleting messages manually.")
            } else {
                uiState = ChatUiState.Error("Could not send. Check your connection.")
            }
        }
    }

    fun deleteMessage(id: String) {
        viewModelScope.launch {
            repository.deleteMessage(id)
            load()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                val db = ChatDatabase.get(context)
                ChatViewModel(
                    ChatRepositoryImpl(NetworkModule.chatApi, db.messageDao())
                )
            }
        }
    }
}
