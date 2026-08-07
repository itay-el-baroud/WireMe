@file:OptIn(ExperimentalMaterial3Api::class)

package com.wireme.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wireme.app.api.RetrofitClient
import com.wireme.app.model.Chat
import kotlinx.coroutines.launch

@Composable
fun ChatsListScreen(navController: NavController) {
    var chats by remember { mutableStateOf<List<Chat>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitClient.api.getChats("current_user@email.com")
                if (response.isSuccessful) {
                    chats = response.body()?.get("chats") ?: emptyList()
                } else {
                    error = "خطأ في تحميل الدردشات"
                }
            } catch (e: Exception) {
                error = "مشكلة في الاتصال"
            } finally {
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الدردشات") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = error!!)
                        Button(onClick = { loading = true; error = null }) {
                            Text("إعادة المحاولة")
                        }
                    }
                }
                chats.isEmpty() -> Text(
                    text = "مفيش دردشات",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn {
                    items(chats) { chat ->
                        ChatItem(chat = chat, onClick = {
                            navController.navigate("chat/${chat.chat_with}/${chat.chat_with}")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = chat.chat_with,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = chat.last_message,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
            Text(
                text = chat.last_time.substring(11, 16),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
