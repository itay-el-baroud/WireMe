package com.wireme.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wireme.app.api.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun ForgotScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("نسيت كلمة المرور") },
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
            when (step) {
                1 -> {
                    Text("أدخل بريدك الإلكتروني")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("البريد الإلكتروني") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (email.isBlank()) {
                                error = "اكتب الإيميل"
                                return@Button
                            }
                            loading = true
                            scope.launch {
                                try {
                                    val response = RetrofitClient.api.forgotPassword(email)
                                    if (response.isSuccessful && response.body()?.success == true) {
                                        snackbarHostState.showSnackbar("تم إرسال الكود")
                                        step = 2
                                    } else {
                                        error = response.body()?.message ?: "خطأ"
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
                        if (loading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        else Text("إرسال الكود")
                    }
                }
                2 -> {
                    Text("أدخل الكود والباسورد الجديد")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("كود التحقق") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("باسورد جديد") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (code.length != 6 || newPassword.length < 6) {
                                error = "اكمل البيانات صح"
                                return@Button
                            }
                            loading = true
                            scope.launch {
                                try {
                                    val response = RetrofitClient.api.verifyOtp(email, code)
                                    if (response.isSuccessful && response.body()?.success == true) {
                                        snackbarHostState.showSnackbar("تم تغيير الباسورد")
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
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
                        if (loading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        else Text("تأكيد")
                    }
                }
            }
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
