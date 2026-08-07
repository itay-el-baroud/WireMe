@file:OptIn(ExperimentalMaterial3Api::class)

package com.wireme.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PrivacyScreen(navController: NavController) {
    var lastSeen by remember { mutableStateOf("everyone") }
    var online by remember { mutableStateOf("everyone") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الخصوصية") },
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
            ListItem(
                headlineContent = { Text("آخر ظهور") },
                supportingContent = { Text(lastSeen) }
            )
            Divider()
            ListItem(
                headlineContent = { Text("متصل الآن") },
                supportingContent = { Text(online) }
            )
        }
    }
}
