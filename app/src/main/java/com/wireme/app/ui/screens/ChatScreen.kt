@file:OptIn(ExperimentalMaterial3Api::class)

package com.wireme.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wireme.app.api.RetrofitClient
import com.wireme.app.model.Message
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(navController: NavController, otherEmail: String, otherName: String) {
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var newMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        scope.launch {
            loadMessages(otherEmail) { 
                messages = it
                loading = false
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(otherName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                    messages.isEmpty() -> Text(
                        text = "ابدأ المحادثة",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    else -> LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        reverseLayout = false
                    ) {
                        items(messages) { message ->
                            MessageBubble(message = message)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
            
            Divider()
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    placeholder = { Text("اكتب رسالة...") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    modifier = Modifier.weight(1f),
                    maxLines = 4
                )
                
                IconButton(
                    onClick = {
                        if (newMessage.isBlank()) return@IconButton
                        val msg = newMessage
                        newMessage = ""
                        scope.launch {
                            try {
                                val response = RetrofitClient.api.sendMessage(
                                    sender = "current_user@email.com",
                                    receiver = otherEmail,
                                    message = msg
                                )
                                if (response.isSuccessful) {
                                    loadMessages(otherEmail) { 
                                        messages = it
                                    }
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("فشل الإرسال")
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Send, contentDescription = "إرسال")
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val isMe = message.sender_email == "current_user@email.com"
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isMe) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Column {
                Text(
                    text = message.message,
                    color = if (isMe) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = message.created_at.substring(11, 16),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

private suspend fun loadMessages(
    otherEmail: String,
    onResult: (List<Message>) -> Unit
) {
    try {
        val response = RetrofitClient.api.getMessages(
            userEmail = "current_user@email.com",
            otherEmail = otherEmail
        )
        if (response.isSuccessful) {
            onResult(response.body()?.get("messages") ?: emptyList())
        } else {
            onResult(emptyList())
        }
    } catch (e: Exception) {
        onResult(emptyList())
    }
}
