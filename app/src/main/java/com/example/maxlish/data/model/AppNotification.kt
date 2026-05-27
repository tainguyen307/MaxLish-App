package com.example.maxlish.data.model

data class AppNotification(

    val notificationId: String = "",

    val userId: String = "",

    val title: String = "",
    val message: String = "",

    val isRead: Boolean = false,

    val createdAt: Long = System.currentTimeMillis()
)