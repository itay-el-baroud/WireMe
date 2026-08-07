package com.wireme.app.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WireMeFirebaseService : FirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // هنا نعمل إشعار لما يجي رسالة جديدة
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // هنا نبعت التوكن للسيرفر
    }
}
