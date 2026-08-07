package com.wireme.app.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WireMeFirebaseService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            // TODO: Show notification
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Send token to server
    }
}
