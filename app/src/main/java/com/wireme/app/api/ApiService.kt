package com.wireme.app.api

import com.wireme.app.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("send_email.php")
    suspend fun sendOtp(
        @Field("email") email: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("verification.php")
    suspend fun verifyOtp(
        @Field("email") email: String,
        @Field("code") code: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("forgot_password.php")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("users.php")
    suspend fun getUsers(): Response<Map<String, List<User>>>

    @FormUrlEncoded
    @POST("search.php")
    suspend fun searchUsers(
        @Field("query") query: String
    ): Response<Map<String, List<User>>>

    @FormUrlEncoded
    @POST("profile.php")
    suspend fun getProfile(
        @Field("email") email: String
    ): Response<Map<String, User>>

    @FormUrlEncoded
    @POST("update_profile.php")
    suspend fun updateProfile(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("image_url") imageUrl: String? = null
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("send_message.php")
    suspend fun sendMessage(
        @Field("sender_email") sender: String,
        @Field("receiver_email") receiver: String,
        @Field("message") message: String,
        @Field("type") type: String = "text"
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("get_messages.php")
    suspend fun getMessages(
        @Field("user_email") userEmail: String,
        @Field("other_email") otherEmail: String
    ): Response<Map<String, List<Message>>>

    @FormUrlEncoded
    @POST("get_chats.php")
    suspend fun getChats(
        @Field("email") email: String
    ): Response<Map<String, List<Chat>>>

    @FormUrlEncoded
    @POST("mark_read.php")
    suspend fun markRead(
        @Field("message_id") messageId: Int
    ): Response<ApiResponse>
}
