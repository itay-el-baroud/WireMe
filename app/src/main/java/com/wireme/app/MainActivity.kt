package com.wireme.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wireme.app.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = androidx.compose.ui.graphics.Color(0xFF2563EB),
                    secondary = androidx.compose.ui.graphics.Color(0xFF3B82F6),
                    background = androidx.compose.ui.graphics.Color(0xFF0F172A),
                    surface = androidx.compose.ui.graphics.Color(0xFF1E293B),
                    onPrimary = androidx.compose.ui.graphics.Color.White,
                    onBackground = androidx.compose.ui.graphics.Color(0xFFF8FAFC),
                    onSurface = androidx.compose.ui.graphics.Color(0xFFF8FAFC)
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
}
