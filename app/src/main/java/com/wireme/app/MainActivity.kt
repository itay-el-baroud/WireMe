package com.wireme.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wireme.app.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WireMeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("register") { RegisterScreen(navController) }
                    composable("otp/{email}") { backStackEntry ->
                        OtpScreen(navController, backStackEntry.arguments?.getString("email") ?: "")
                    }
                    composable("forgot") { ForgotScreen(navController) }
                    composable("home") { HomeScreen(navController) }
                    composable("chat/{email}/{name}") { backStackEntry ->
                        ChatScreen(
                            navController,
                            backStackEntry.arguments?.getString("email") ?: "",
                            backStackEntry.arguments?.getString("name") ?: ""
                        )
                    }
                    composable("chats") { ChatsListScreen(navController) }
                    composable("settings") { SettingsScreen(navController) }
                    composable("profile") { ProfileScreen(navController) }
                    composable("privacy") { PrivacyScreen(navController) }
                    composable("blocked") { BlockedScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun WireMeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = MaterialTheme.colorScheme.primary,
            secondary = MaterialTheme.colorScheme.secondary,
            background = MaterialTheme.colorScheme.background
        ),
        content = content
    )
}
