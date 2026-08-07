package com.wireme.app.model

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val image_url: String? = null,
    val bio: String? = null,
    val is_online: Int = 0,
    val last_seen: String? = null
)

data class Message(
    val id: Int,
    val sender_email: String,
    val receiver_email: String,
    val message: String,
    val type: String = "text",
    val is_read: Int = 0,
    val created_at: String
)

data class Chat(
    val chat_with: String,
    val last_time: String,
    val last_message: String
)
