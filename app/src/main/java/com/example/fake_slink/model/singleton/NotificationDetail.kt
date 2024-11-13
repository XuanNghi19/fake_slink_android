package com.example.fake_slink.model.singleton

import com.example.fake_slink.notification.Notification

object NotificationDetail {
    private var notificationDetail: Notification? = null

    fun login(notification: Notification?) {
        this.notificationDetail = notification
    }

    fun logout() {
        this.notificationDetail = null
    }

    fun get(): Notification? {
        return this.notificationDetail
    }
}