package com.liceo.liceochat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.liceo.liceochat.domain.Message

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory)
) {
    ChatContent(
        uiState = viewModel.uiState,
        myName = viewModel.myName,
        draft = viewModel.draft,
        onNameChange = viewModel::onNameChange,
        onDraftChange = viewModel::onDraftChange,
        onSend = viewModel::send,
        onRefresh = viewModel::load
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatContent(
    uiState: ChatUiState,
    myName: String,
    draft: String,
    onNameChange: (String) -> Unit = {},
    onDraftChange: (String) -> Unit = {},
    onSend: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LiceoChat") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // TODO 12: draw four states
                when (uiState) {
                    ChatUiState.Loading -> {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    ChatUiState.Empty -> {
                        Text(
                            text = "No messages yet. Say hello!",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is ChatUiState.Ready -> {
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(uiState.messages, key = { it.id }) { message ->
                                MessageRow(message)
                            }
                        }
                    }
                    is ChatUiState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(uiState.message)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onRefresh) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }

            MessageInput(
                name = myName,
                draft = draft,
                onNameChange = onNameChange,
                onDraftChange = onDraftChange,
                onSend = onSend
            )
        }
    }
}

@Composable
fun MessageRow(message: Message) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(message.sender, style = MaterialTheme.typography.labelLarge)
        Text(message.text, style = MaterialTheme.typography.bodyMedium)
    }
    HorizontalDivider()
}

@Composable
fun MessageInput(
    name: String,
    draft: String,
    onNameChange: (String) -> Unit,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(12.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Your full name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChange,
                label = { Text("Message") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = onSend) { Text("Send") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenReadyPreview() {
    MaterialTheme {
        ChatContent(
            uiState = ChatUiState.Ready(
                listOf(
                    Message(id = "1", sender = "Alice", text = "Hello everyone!", createdAt = 1000000L),
                    Message(id = "2", sender = "Bob", text = "Hi Alice! How are you?", createdAt = 1000005L)
                )
            ),
            myName = "Juan Dela Cruz",
            draft = "Hello team!"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenEmptyPreview() {
    MaterialTheme {
        ChatContent(
            uiState = ChatUiState.Empty,
            myName = "Juan Dela Cruz",
            draft = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenErrorPreview() {
    MaterialTheme {
        ChatContent(
            uiState = ChatUiState.Error("No internet connection."),
            myName = "Juan Dela Cruz",
            draft = "Test message"
        )
    }
}
