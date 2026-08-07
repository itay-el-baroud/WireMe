package com.wireme.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wireme.app.api.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(navController: NavController, email: String) {
    var code by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("تأكيد البريد") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("رجوع")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "تم إرسال كود التحقق إلى",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = email,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = code,
                onValueChange = { 
                    if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                        code = it
                    }
                },
                label = { Text("كود التحقق") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    letterSpacing = 8.sp
                ),
                singleLine = true
            )
            
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    if (code.length != 6) {
                        error = "الكود لازم 6 أرقام"
                        return@Button
                    }
                    loading = true
                    error = null
                    scope.launch {
                        try {
                            val response = RetrofitClient.api.verifyOtp(email, code)
                            if (response.isSuccessful && response.body()?.success == true) {
                                snackbarHostState.showSnackbar("تم التحقق بنجاح")
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                error = response.body()?.message ?: "كود غلط"
                            }
                        } catch (e: Exception) {
                            error = "مشكلة في الاتصال"
                        } finally {
                            loading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("تحقق")
                }
            }
            
            TextButton(
                onClick = {
                    scope.launch {
                        try {
                            RetrofitClient.api.sendOtp(email)
                            snackbarHostState.showSnackbar("تم إعادة الإرسال")
                        } catch (e: Exception) {
                            error = "مشكلة في الإرسال"
                        }
                    }
                }
            ) {
                Text("إعادة إرسال الكود")
            }
        }
    }
}
