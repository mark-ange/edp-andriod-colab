package com.liceo.mysocial.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.liceo.mysocial.data.Post
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(vm: PostsViewModel) {

    val posts by vm.posts.collectAsStateWithLifecycle()

    var editing by remember { mutableStateOf<Post?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<Post?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MySocial (${posts.size} posts)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { editing = null; showEditor = true }) {
                Icon(Icons.Default.Add, contentDescription = "New post")
            }
        }
    ) { padding ->
        if (posts.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("No posts yet. Tap + to write your first one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(posts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onEdit = { editing = post; showEditor = true },
                        onDelete = { deleting = post },
                    )
                }
            }
        }
    }

    if (showEditor) {
        PostEditorDialog(
            initialText = editing?.content ?: "",
            onDismiss = { showEditor = false },
            onSave = { newText ->
                val current = editing
                if (current == null) vm.addPost(newText) else vm.editPost(current, newText)
                showEditor = false
            },
        )
    }

    if (deleting != null) {
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Delete post?") },
            text = { Text("This post will be gone forever.") },
            confirmButton = {
                TextButton(onClick = { 
                    deleting?.let { vm.deletePost(it) }
                    deleting = null 
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleting = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PostCard(post: Post, onEdit: () -> Unit, onDelete: () -> Unit) {
    val time = remember(post.createdAt) {
        SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(post.createdAt))
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(post.content, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(6.dp))
            Text(time, style = MaterialTheme.typography.labelSmall)
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}

@Composable
fun PostEditorDialog(
    initialText: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf(initialText) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialText.isEmpty()) "New post" else "Edit post") },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = { text = it },
                label = { Text("What's on your mind?") },
                minLines = 3,
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(text.trim()) }, enabled = text.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
